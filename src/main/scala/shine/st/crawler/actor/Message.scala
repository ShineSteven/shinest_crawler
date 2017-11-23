package shine.st.crawler.actor

import akka.actor.ActorRef
import org.joda.time.DateTime
import shine.st.crawler.data.web.BoxOfficeCemojo.Link

object Message {

  case object Start

  case object End

  case object Check

  case class Wait(waitSecond: Long)

  case object Flush

  case class QueryDate(date: DateTime) {
    override def hashCode = (date.getDayOfYear, date.getYear).##

    override def equals(other: Any) = other match {
      case that: QueryDate =>
        (that canEqual this) &&
          (this.date.getDayOfYear == that.date.getDayOfYear) && (this.date.getYear == that.date.getYear)
      case _ =>
        false
    }

    def canEqual(other: Any) = other.isInstanceOf[QueryDate]
  }

  case class QueryWeek(year: Int, week: Int) {
    override def hashCode = (year, week).##

    override def equals(other: Any) = other match {
      case that: QueryWeek =>
        (that canEqual this) &&
          (this.week == that.week) && (this.year == that.year)
      case _ =>
        false
    }

    def canEqual(other: Any) = other.isInstanceOf[QueryWeek]
  }

  case class QueryMovie(link: Link)

  case class QueryDateRecords(date: QueryDate, count: Int)

  case class QueryWeekRecords(week: QueryWeek, count: Int)

  case class Deploy(count: Int)

  case class Remote(actorRef: Vector[ActorRef])

}
