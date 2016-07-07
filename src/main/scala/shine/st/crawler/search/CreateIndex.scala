package shine.st.crawler.search

import com.sksamuel.elastic4s.source.Indexable
import shine.st.crawler.model.CrawlerModel.MovieInfo

/**
  * Created by stevenfanchiang on 2016/7/7.
  */
object CreateIndex {
  implicit object MovieIndexable extends Indexable[MovieInfo] {
    override def json(t: MovieInfo): String = s"""{"movie_name" : "${t.movieName}, "MPAA" : "${t.MPAA}", "genre" : "${t.genre}", "runtime" : ${t.runTime}, "budget":  """
//    case class MovieInfo(movieName: String, MPAA: String, genre: String, runTime: Int, budget: Int, studio: String)
  }
}
