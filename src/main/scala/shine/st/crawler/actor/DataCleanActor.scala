package shine.st.crawler.actor

import akka.actor.Props
import shine.st.crawler.Dump
import shine.st.crawler.model.CrawlerModel.{DailyInfo, MovieInfo}
import shine.st.crawler.model.MovieModel.{Daily, Movie}

/**
  * Created by shinest on 2016/7/16.
  */
class DataCleanActor extends CommonActor {

  override def realReceive: Receive = {
    case d: DailyInfo =>
      sender ! Daily(d.date, d.gross, Movie(d.movieInfo.title.hashCode, d.movieInfo.title, None, None, None, None, None), d.regionName, d.theaters)

    case m: MovieInfo =>
      Dump.logger.debug("return movie model")
      sender ! Movie(m.movieName.hashCode, m.movieName, Option(m.MPAA), Option(m.genre), Option(m.runTime), Option(m.budget), Option(m.studio))
  }
}

object DataCleanActor {
  def props(): Props = Props[DataCleanActor]
}
