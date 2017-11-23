package shine.st.crawler

import org.jsoup.nodes.{Document, Element}
import org.slf4j.LoggerFactory
import shine.st.common.{DateTimeUtils, NumberUtils}
import shine.st.crawler.actor.Message.QueryWeek
import shine.st.crawler.data.web.BoxOfficeCemojo
import shine.st.crawler.data.web.BoxOfficeCemojo._

import scala.collection.JavaConversions._

/**
  * Created by shinest on 2016/7/2.
  */
object BoxOfficeCemojoParser {
  val log = LoggerFactory.getLogger(BoxOfficeCemojoParser.getClass)

  def movie(doc: Document) = {
    def extractMinutes(time: String) = {
      val repleactTime = time.replace(" ", "")

      if (repleactTime == "N/A")
        0
      else {
        val hour = repleactTime.substring(0, repleactTime.indexOf("hrs."))
        val minutes = repleactTime.substring(repleactTime.indexOf("hrs.") + 4, repleactTime.indexOf("min."))
        NumberUtils.strTrans[Int](hour).getOrElse(0) * 60 + NumberUtils.strTrans[Int](minutes).getOrElse(0)
      }
    }

    val movieName = doc.select("#body table tbody tr td table tbody tr td font b").get(1).text

    val table = doc.select("#body table tbody tr td table tbody tr td table tbody tr td center table").head

    val trs = table.select("tr").toList.tail
    val tds = trs.flatMap(_.select("td").map(td => {
      val text = td.text()
      text.substring(0, text.indexOf(":")) -> td.select("b")
    })).toMap

    BoxOfficeCemojo.Movie(movieName, tds("MPAA Rating").head.text, tds("Genre").head.text, extractMinutes(tds("Runtime").head.text), tds("Production Budget").head.text, extractLink(tds("Distributor").head), DateTimeUtils.parse(tds("Release Date").head.text)("MMMM dd, yyyy", DateTimeUtils.UTC).get)
  }

  def daily(doc: Document) = {
    val time = DateTimeUtils.parseEnDate(doc.select("#body center center font b").get(0).text()).get

    val table = doc.select("#body center center table tbody tr td table").get(1)

    val trs = table.select("tr").toList.tail

    val dailyInfo = trs.map { tr =>
      val tds = tr.select("td")
      Daily(NumberUtils.strTrans[Int](tds.get(0).text).getOrElse(0), time, extractLink(tds.get(2)), extractLink(tds.get(3)), NumberUtils.strTrans[Int](tds.get(4).text).getOrElse(0), NumberUtils.strTrans[Int](tds.get(7).text).getOrElse(0))
    }
    dailyInfo
  }


  def weekly(queryWeek: QueryWeek)(doc: Document) = {
    val table = doc.select("#body center center table tbody tr td table").get(0)
    val trs = table.select("tr").toList.drop(3).dropRight(1)

    val weeklyInfo = trs.map { tr =>
      val tds = tr.select("td")

      Weekly(queryWeek.year, queryWeek.week, NumberUtils.strTrans[Int](tds.get(0).text).getOrElse(-1), NumberUtils.strTrans[Int](tds.get(9).text).getOrElse(0),
        NumberUtils.strTrans[Int](tds.get(3).text).getOrElse(0),
        NumberUtils.strTrans[Int](tds.get(6).text).getOrElse(0),
        NumberUtils.strTrans[Int](tds.get(4).text).getOrElse(0),
        NumberUtils.strTrans[Int](tds.get(8).text).getOrElse(0),
        extractLink(tds.get(1)), extractLink(tds.get(2)))
    }
    weeklyInfo
  }

  def extractLink(e: Element) = {
    val a = e.select("a").head
    Link(a.text(), a.attr("abs:href"))
  }
}
