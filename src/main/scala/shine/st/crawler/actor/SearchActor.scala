package shine.st.crawler.actor

import akka.actor.Props
import shine.st.crawler._
import shine.st.crawler.actor.MovieCrawlerActor.Complete
import shine.st.crawler.model.MovieModel
import shine.st.crawler.search.SearchUtils

/**
  * Created by shinest on 2016/7/16.
  */
class SearchActor extends CommonActor {
  override def realReceive: Receive = {
    case d: MovieModel.Daily =>
      SearchUtils.createIndex("movie_predict", "daily", d.movie.id + d.date.hashCode(), d)
      sender ! Complete(1)

    case m: MovieModel.Movie =>
      SearchUtils.createIndex("movie_predict", "movie", m.id, m)
      sender ! Complete(1)
  }
}

object SearchActor {
  def props(): Props = Props[SearchActor]
}
