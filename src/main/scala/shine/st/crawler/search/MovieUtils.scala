package shine.st.crawler.search

import com.sksamuel.elastic4s.ElasticDsl._

/**
  * Created by stevenfanchiang on 2016/7/20.
  */
object MovieUtils {
  def findMovie(movieName: String) = {
    SearchUtils.query("movie_predict", "movie", termQuery("movie_name.raw", movieName))
  }
}
