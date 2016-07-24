package shine.st.crawler.actor

import akka.actor.Props
import com.typesafe.config.ConfigFactory
import org.joda.time.DateTime
import shine.st.common.{ConfigUtils, DateTimeUtils}
import shine.st.crawler.actor.MovieCrawlerActor.Complete
import shine.st.crawler.model.CrawlerModel.Link
import shine.st.crawler.{Dump, MovieSelector, WebCrawler}

import scala.collection.JavaConversions._

/**
  * Created by shinest on 2016/7/16.
  */
class WebQueryActor extends CommonActor {


  override def realReceive: Receive = {
    case WebQueryActor.Daily(d) =>

      val parameter = WebQueryActor.dailyParameter + ("sortdate" -> d.toString(DateTimeUtils.dateFormat))
      val dailyInfo = WebCrawler.query(WebQueryActor.dailyUrl, parameter)(MovieSelector.daily)

      dailyInfo match {
        case Some(e) =>
          sender ! MovieCrawlerActor.DailyList(e)
        case None =>
      }

    case WebQueryActor.Movie(link) =>
      Dump.logger.debug(link.toString)
      WebCrawler.query(link.link)(MovieSelector.movie) match {
        case Some(m) =>
          sender ! m
        case None => Dump.logger.debug("movie query none....")
          sender ! Complete(1)
      }
  }
}

object WebQueryActor {
  val config = ConfigFactory.load
  val dailyUrl = config.getString("movie.daily.path")
  val dailyParameter = ConfigUtils.configToMap(config.getConfigList("movie.daily.parameter").toList)

  def props(): Props = Props[WebQueryActor]

  case class Daily(d: DateTime)

  case class Movie(link: Link)

}