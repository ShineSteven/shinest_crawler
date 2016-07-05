package shine.st.crawler.model

import org.joda.time.DateTime

/**
  * Created by shinest on 2016/7/2.
  */
object MovieModel {
  case class Daily(date: DateTime, gross: Int, movieId: String, regionId: String, theaters: Int)

  case class DailyInfo(date: DateTime, movieInfo: Link, studio: Link, gross: Int, theaters: Int)

  case class Link(title: String, link: String)

  case class MovieInfo(movieName: String, MPAA: String, genre: String, runTime: Int, budget: Int, studio: String)
}

