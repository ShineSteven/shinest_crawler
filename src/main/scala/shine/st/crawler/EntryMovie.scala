package shine.st.crawler

import akka.actor.ActorSystem
import org.joda.time.{DateTime, DateTimeConstants}
import shine.st.common.{DateTimeUtils, NumberUtils}
import shine.st.crawler.Dump.logger
import shine.st.crawler.actor.MasterActor
import shine.st.crawler.model.Message.{QueryDate, QueryWeek, Start}

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Created by shinest on 2016/7/19.
  */
object EntryMovie {
  val defaultDate = new DateTime().minusDays(3).secondOfDay().withMinimumValue()

  def main(args: Array[String]): Unit = {
    logger.debug("movie crawler main start...")

    val now = DateTimeUtils.now
    val defaultDate = now.minusDays(2)
    val defaultWeek = movieWeek(now.minusDays(9))

    val (weeklyList: List[QueryWeek], dailyList: List[QueryDate]) = {
      if (args.length == 0)
        (Nil, List(QueryDate(defaultDate)))
      else {
        args(0) match {
          case "Y" =>
            val specificYear = NumberUtils.strTrans[Int](args(1)).getOrElse(-1)

            val startMovieDate = firstMovieDateOfYear(specificYear)

            val endMovieDate = {
              if (specificYear == now.getYear)
                movieDate(defaultWeek._1, defaultWeek._2).takeRight(1).head
              else
                firstMovieDateOfYear(specificYear + 1).minusDays(1)
            }

            queryPirod(startMovieDate, endMovieDate)

          case "YR" =>
            val start = NumberUtils.strTrans[Int](args(1)).getOrElse(-1)
            val end = NumberUtils.strTrans[Int](args(2)).getOrElse(-1)

            val startMovieDate = firstMovieDateOfYear(start)

            val endMovieDate = {
              if (end == now.getYear)
                movieDate(defaultWeek._1, defaultWeek._2).takeRight(1).head
              else
                firstMovieDateOfYear(end + 1).minusDays(1)
            }

            queryPirod(startMovieDate, endMovieDate)


          case "W" =>
            val year = NumberUtils.strTrans[Int](args(1)).getOrElse(-1)
            val week = NumberUtils.strTrans[Int](args(2)).getOrElse(-1)

            (List(QueryWeek(year, week)), Nil)

          case "WD" =>
            val year = NumberUtils.strTrans[Int](args(1)).getOrElse(-1)
            val week = NumberUtils.strTrans[Int](args(2)).getOrElse(-1)

            (List(QueryWeek(year, week)), movieDate(year, week).map(QueryDate(_)))

          case "WR" =>
            val startYear = NumberUtils.strTrans[Int](args(1)).getOrElse(-1)
            val startWeek = NumberUtils.strTrans[Int](args(2)).getOrElse(-1)
            val endYear = NumberUtils.strTrans[Int](args(3)).getOrElse(-1)
            val endWeek = NumberUtils.strTrans[Int](args(4)).getOrElse(-1)

            val startMovieDate = movieDate(startYear, startWeek).head
            val endMovieDate = movieDate(endYear, endWeek).takeRight(1).head

            (queryPirod(startMovieDate, endMovieDate)._1, Nil)

          case "WRD" =>
            val startYear = NumberUtils.strTrans[Int](args(1)).getOrElse(-1)
            val startWeek = NumberUtils.strTrans[Int](args(2)).getOrElse(-1)
            val endYear = NumberUtils.strTrans[Int](args(3)).getOrElse(-1)
            val endWeek = NumberUtils.strTrans[Int](args(4)).getOrElse(-1)

            val startMovieDate = movieDate(startYear, startWeek).head
            val endMovieDate = movieDate(endYear, endWeek).takeRight(1).head

            queryPirod(startMovieDate, endMovieDate)

          case "D" =>
            (Nil, List(QueryDate(DateTimeUtils.parseDate(args(1)).getOrElse(defaultDate))))

        }

      }
    }

    logger.info(s"weekly parameter: $weeklyList")
    logger.info(s"daily parameter: $dailyList")
    val system = ActorSystem("BoxOfficeCrawler")
    import system._

//    val masterActor = system.actorOf(MasterActor(dailyList, weeklyList), "master-actor")

//    masterActor ! Start

    Await.result(system.whenTerminated.map(_ => logger.info("Actor system was shut down")), Duration.Inf)

  }

  def parseDateOrDefault(text: String) = {
    DateTimeUtils.parseDate(text).getOrElse(defaultDate)
  }

  def queryPirod(startMovieDate: DateTime, endMovieDate: DateTime) = {
    val duration = new org.joda.time.Duration(startMovieDate, endMovieDate)
    val r = (0 to duration.getStandardDays.toInt).map {
      i =>
        val theDate = startMovieDate.plusDays(i)
        val (weekYear, week) = movieWeek(theDate)
        (QueryWeek(weekYear, week), QueryDate(theDate))
    }.toList

    (r.map(_._1).toSet.toList, r.map(_._2))
  }

  def movieWeek(date: DateTime) = {
    val tmp = firstMovieDateOfYear(date.getYear)

    val firstMovieDate = {
      if (date.isBefore(tmp))
        firstMovieDateOfYear(date.getYear - 1)
      else
        tmp
    }

    val duration = new org.joda.time.Duration(firstMovieDate, date)
    (firstMovieDate.getYear, (duration.getStandardDays / 7 + 1).toInt)
  }

  def movieDate(year: Int, week: Int) = {
    val firstMovieDate = firstMovieDateOfYear(year)

    (0 to 6).map(firstMovieDate.plusDays((week - 1) * 7).plusDays(_)).toList
  }

  def firstMovieDateOfYear(year: Int) = {
    val oneJanuary = DateTimeUtils.now.withDate(year, 1, 1).withMillisOfDay(0)

    oneJanuary.getDayOfWeek match {
      case DateTimeConstants.MONDAY | DateTimeConstants.THURSDAY | DateTimeConstants.WEDNESDAY | DateTimeConstants.TUESDAY =>
        oneJanuary.withDayOfWeek(DateTimeConstants.FRIDAY)
      case DateTimeConstants.FRIDAY => oneJanuary
      case DateTimeConstants.SATURDAY | DateTimeConstants.SUNDAY =>
        oneJanuary.plusMonths(1).withWeekOfWeekyear(1).withDayOfWeek(DateTimeConstants.FRIDAY)
    }
  }
}
