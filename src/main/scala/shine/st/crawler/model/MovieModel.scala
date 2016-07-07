package shine.st.crawler.model

import org.joda.time.DateTime

/**
  * Created by shinest on 2016/7/2.
  */
object MovieModel {
  case class Movie(movieName: String, MPAA: String, genre: String, runTime: Int, budget: Int, studio: String)

  case class Daily(date: DateTime, gross: Int, movieId: String, regionId: String, theaters: Int)

  case class Release(date: DateTime, regionId: String)

  case class Region(regionName: String)

}




