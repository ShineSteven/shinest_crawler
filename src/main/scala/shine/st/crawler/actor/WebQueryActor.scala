package shine.st.crawler.actor

import akka.actor.Props
import com.typesafe.config.ConfigFactory
import shine.st.common.{ConfigUtils, DateTimeUtils}
import shine.st.crawler.actor.Message._
import shine.st.crawler.{BoxOfficeCemojoParser, Dump, WebCrawler}

import scala.collection.JavaConversions._


/**
  * Created by shinest on 2016/7/16.
  */
class WebQueryActor extends CommonActor {
  val config = ConfigFactory.load
  val dailyUrl = config.getString("movie.daily.path")
  val weeklyUrl = config.getString("movie.weekly.path")
  val dailyParameter = ConfigUtils.configToMap(config.getConfigList("movie.daily.parameter").toList)
  val weeklyParameter = ConfigUtils.configToMap(config.getConfigList("movie.weekly.parameter").toList)

  def reply[A](data: List[A]) = {
    val theSender = sender
    data.foreach(theSender ! _)
  }

  override def realReceive: Receive = {
    case qd: QueryDate =>

      val parameter = dailyParameter + ("sortdate" -> DateTimeUtils.formatDate(qd.date))
      val daily = WebCrawler.query(dailyUrl, parameter)(BoxOfficeCemojoParser.daily)

      daily match {
        case Some(dailyList) =>
          reply(dailyList)
          sender ! QueryDateRecords(qd, dailyList.size.toShort)
        case None =>
        //          TODO: None flow
      }

    case qw: QueryWeek =>
      val parameters = weeklyParameter + ("wk" -> qw.week) + ("yr" -> qw.year)
      val weekly = WebCrawler.query(weeklyUrl, parameters)(BoxOfficeCemojoParser.weekly(qw))

      weekly match {
        case Some(weeklyList) =>
          reply(weeklyList)
          sender ! QueryWeekRecords(qw, weeklyList.size.toShort)
        case None =>
        //          TODO: None flow
      }

    case QueryMovie(link) =>
      Dump.logger.debug(link.toString)
      WebCrawler.query(link.link)(BoxOfficeCemojoParser.movie) match {
        case Some(m) =>
          sender ! m
        case None =>
        //          TODO: None flow
      }
  }
}

object WebQueryActor {
  def props(): Props = Props[WebQueryActor]
}