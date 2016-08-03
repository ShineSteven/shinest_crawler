package shine.st.crawler

import akka.actor.ActorSystem
import org.joda.time.DateTime
import shine.st.common.DateTimeUtils
import shine.st.common.akka.Message.Check
import shine.st.crawler.actor.{BigBoss, MovieCrawlerActor}
import shine.st.crawler.actor.MovieCrawlerActor.CrawlerDate
import Dump.logger

/**
  * Created by stevenfanchiang on 2016/7/19.
  */
object MovieMain {
  val defaultDate = new DateTime().minusDays(3).secondOfDay().withMinimumValue()

  def main(args: Array[String]): Unit = {
    logger.debug("movie crawler main start...")

    val (startDate, endDate) = args.length match {
      case 0 => (defaultDate,defaultDate)
      case 1 => (parseDateOrDefault(args(0)), defaultDate)
      case 2 => (parseDateOrDefault(args(0)), parseDateOrDefault(args(1)))
      case _ => throw new Exception("wrong argument size")
    }

    if (endDate.isAfter(defaultDate)) {
      logger.debug("結束日期需小於3天前")
      System.exit(-1)
    }

    val different = (endDate.getDayOfYear - startDate.getDayOfYear)
    val dateList = (for (i <- 0 to different) yield {
      startDate.plusDays(i)
    }).toList

    val system = ActorSystem("CrawlerMovie")
    val movieActor = system.actorOf(MovieCrawlerActor.props, "movieActor")
    val bigBossActor = system.actorOf(BigBoss(movieActor), "bigBossActor")

    Dump.logger.debug(s"crawler date range ${
      DateTimeUtils.format(startDate)(DateTimeUtils.DATE_PATTERN)
    } ~ ${
      DateTimeUtils.format(endDate)(DateTimeUtils.DATE_PATTERN)
    }")

    Dump.logger.debug(dateList.toString)
    movieActor ! CrawlerDate(dateList)
    //    movieActor ! CrawlerDate(List.empty[DateTime])
    bigBossActor ! Check

    system.awaitTermination()
  }

  def parseDateOrDefault(text: String) = {
    DateTimeUtils.parseDate(text).getOrElse(defaultDate)
  }
}
