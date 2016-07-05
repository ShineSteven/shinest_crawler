package shine.st.crawler.tmp


import org.slf4j.LoggerFactory
import shine.st.crawler.crawler.{MovieSelector, WebCrawler}

/**
  * Created by stevenfanchiang on 2016/6/22.
  */
object Main {
  val log = LoggerFactory.getLogger(Main.getClass)

  def main(args: Array[String]): Unit = {
    val movieInfo = WebCrawler.query("http://www.boxofficemojo.com/movies/?page=daily&id=freestateofjones.htm")(MovieSelector.movie)
    val dailyInfo = WebCrawler.query("http://www.boxofficemojo.com/daily/chart/?view=1day&sortdate=2016-06-28&p=.htm")(MovieSelector.daily)

    log.debug(movieInfo.toString)

    log.debug(dailyInfo.toString)
  }


}
