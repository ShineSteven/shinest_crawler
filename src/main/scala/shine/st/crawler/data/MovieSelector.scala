package shine.st.crawler

import org.joda.time.DateTime
import org.jsoup.nodes.{Document, Element}
import org.slf4j.LoggerFactory
import shine.st.common.NumberUtils
import shine.st.crawler.model.CrawlerModel.{DailyInfo, Link, MovieInfo}

import scala.collection.JavaConversions._

/**
  * Created by shinest on 2016/7/2.
  */
object MovieSelector {
  val log = LoggerFactory.getLogger(MovieSelector.getClass)

  def movie(doc: Document) = {
    def extractMinutes(time: String) = {
      val repleactTime = time.replace(" ", "")

      val hour = repleactTime.substring(0, repleactTime.indexOf("hrs."))
      val minutes = repleactTime.substring(repleactTime.indexOf("hrs.") + 4, repleactTime.indexOf("min."))
      NumberUtils.stringToInt(hour).getOrElse(0) * 60 + NumberUtils.stringToInt(minutes).getOrElse(0)
    }

    val movieName = doc.select("#body table tbody tr td table tbody tr td font b").get(1).text
    log.debug(movieName)

    val table = doc.select("#body table tbody tr td table tbody tr td table tbody tr td center table").head
    val trs = table.select("tr").toList.tail

    val tds = trs.flatMap { tr => tr.select("td").map { td =>
      val text = td.text()
      text.substring(0, text.indexOf(":")) -> text.substring(text.indexOf(":") + 2)
    }
    }.toMap

    MovieInfo(movieName, tds("MPAA Rating"), tds("Genre"), extractMinutes(tds("Runtime")), NumberUtils.unitsOfMoneyToInt(tds("Production Budget")), tds("Distributor"))
  }

  def daily(doc: Document) = {
    def extractLink(e: Element) = {
      val a = e.select("a").head
      Link(a.text(), a.attr("abs:href"))
    }

    val time = new DateTime()

    val table = doc.select("#body center center table tr td table").get(1)

    val trs = table.select("tr").toList.tail

    val dailyInfo = trs.map { tr =>
      val tds = tr.select("td")
      DailyInfo(time, extractLink(tds.get(2)), extractLink(tds.get(3)), NumberUtils.stringToInt(tds.get(4).text.substring(1).replace(",", "")).getOrElse(0), NumberUtils.stringToInt(tds.get(7).text).getOrElse(0))
    }
    dailyInfo
  }
}
