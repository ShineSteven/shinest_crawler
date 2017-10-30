package shine.st.crawler.actor

import akka.actor.Props
import com.sksamuel.elastic4s.circe._
import shine.st.common.{DateTimeUtils, HashUtils}
import shine.st.crawler.Dump
import shine.st.crawler.actor.Message._
import shine.st.crawler.data.index.BoxOffice
import shine.st.crawler.data.index.BoxOffice._
import shine.st.crawler.data.web.BoxOfficeCemojo
import shine.st.crawler.search.ElasticsearchUtils

import scala.collection.mutable

/**
  * Created by shinest on 2016/7/12.
  */

class MovieActor extends CommonActor {
  val webQueryActor = context.actorOf(WebQueryActor.props, "query-actor")
  //  val cleanActor = context.actorOf(CleanActor.props, "clean-actor")
  val elasticsearchActor = context.actorOf(ElasticsearchActor.props, "elasticsearch-actor")

  val bigBossActor = context.actorOf(BigBoss(self), "big-boss-actor")


  val queryDateIndexCompleteCount = mutable.Map.empty[QueryDate, Int]
  val queryWeekIndexCompleteCount = mutable.Map.empty[QueryWeek, Int]

  val queryDateQueryCount = mutable.Map.empty[QueryDate, Int]
  val queryWeekQueryCount = mutable.Map.empty[QueryWeek, Int]

  val movies = mutable.Map.empty[String, Movie]

  var creatingMovieCount = 0

  override def preStart(): Unit = {
    bigBossActor ! Check
  }

  override def realReceive: Receive = {

    case d: QueryDate =>
      queryDateIndexCompleteCount.update(d, 0)
      webQueryActor ! d

    case w: QueryWeek =>
      queryWeekIndexCompleteCount.update(w, 0)
      webQueryActor ! w

    case d: QueryDateRecords =>
      queryDateQueryCount.update(d.date, d.count)

    case w: QueryWeekRecords =>
      queryWeekQueryCount.update(w.week, w.count)

    case m: QueryMovie =>
      val queryResult = ElasticsearchUtils.termQuery("box_office", "movie", "_id", HashUtils.sha256(m.link.title))

      queryResult.to[MovieSource].headOption match {
        case Some(movieSource) =>
          val id = queryResult.ids.head
          movies + (movieSource.name -> Movie(Some(movieSource), id))
        case None =>
          creatingMovieCount += 1
          webQueryActor ! m
      }


    case m: BoxOfficeCemojo.Movie =>
      val movieSource = MovieSource(m.name, m.MPAA, m.genre, m.duration, m.budget, m.studio.title, List(Release("US", m.usReleaseDate)))
      val movie = Movie(Option(movieSource), HashUtils.sha256(movieSource.name))
      Dump.logger.debug(s"create movie:${movieSource.name}")
      elasticsearchActor ! movie
      creatingMovieCount -= 1
      movies + (movieSource.name -> movie)


    case d: BoxOfficeCemojo.Daily =>
      val movie = movies.get(d.movieInfo.title) match {
        case Some(m) => m
        case None =>
          self ! QueryMovie(d.movieInfo)
          Movie(None, HashUtils.sha256(d.movieInfo.title))
      }

      elasticsearchActor ! BoxOffice.Daily(Option(DailySource(d.date, d.gross, d.rank, d.theaters, "US", d.studio.title)), movie, HashUtils.sha256(s"${DateTimeUtils.formatDate(d.date)}US${d.movieInfo.title}"))

      val qd = QueryDate(d.date)
      queryDateIndexCompleteCount.update(qd, queryDateIndexCompleteCount(qd) + 1)

    case w: BoxOfficeCemojo.Weekly =>
      val movie = movies.get(w.movieInfo.title) match {
        case Some(m) => m
        case None =>
          self ! QueryMovie(w.movieInfo)
          Movie(None, HashUtils.sha256(w.movieInfo.title))
      }

      elasticsearchActor ! BoxOffice.Weekly(Option(WeeklySource(w.year, w.week, w.onReleaseWeek, w.weeklyGross, w.weekdayGross, w.weekendGross, w.rank, w.theaters, "US", w.studio.title, None)), movie, HashUtils.sha256(s"${w.year}${w.week}US${w.movieInfo.title}"))

      val qw = QueryWeek(w.week, w.year)
      queryWeekIndexCompleteCount.update(qw, queryWeekIndexCompleteCount(qw) + 1)

    case Check =>

      Dump.logger.debug(s"${self.path.name} check complete count: ")
      var counter = 0
      queryDateIndexCompleteCount.foreach { case (date, count) =>
        Dump.logger.debug(s"${self.path.name} $date total query count: ${queryDateQueryCount.getOrElse(date, -1)},complete count: $count")
        if (queryDateQueryCount.getOrElse(date, -1) == count)
          counter += 1
      }

      queryWeekIndexCompleteCount.foreach { case (date, count) =>
        Dump.logger.debug(s"${self.path.name} $date total query count: ${queryWeekQueryCount.getOrElse(date, -1)},complete count: $count")
        if (queryWeekQueryCount.getOrElse(date, -1) == count)
          counter += 1
      }

      if ((queryDateQueryCount.size > 0 || queryWeekQueryCount.size > 0) && queryDateIndexCompleteCount.size + queryWeekIndexCompleteCount.size == counter && creatingMovieCount == 0) {
        Dump.logger.debug(s"${self.path.name} graceful stop movie actor")
        webQueryActor ! End
        elasticsearchActor ! Flush
        sender ! End
        self ! End
      } else {
        sender ! Wait(15)
      }

  }

}

object MovieActor {
  def props(): Props = Props[MovieActor]

  case class Complete(r: Int)

}