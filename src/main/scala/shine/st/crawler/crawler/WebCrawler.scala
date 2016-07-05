package shine.st.crawler.crawler

import org.joda.time.DateTime
import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}
import shine.st.common.NumberUtils

import scala.util.control.NonFatal

/**
  * Created by shinest on 2016/7/2.
  */
object WebCrawler extends Crawler {

  def query[T](uri: String)(f: Document => T): Option[T] = {
    try {
      val doc = Jsoup.connect(uri).get
      Option(f(doc))
    } catch {
      case NonFatal(e) =>
        e.printStackTrace()
        None
    }
  }

}
