package shine.st.crawler.model

import org.joda.time.DateTime
import play.api.libs.functional.syntax._
import play.api.libs.json._
import shine.st.common.DateTimeUtils

/**
  * Created by shinest on 2016/7/2.
  */
object MovieModel {

  //  case class Movie(id: Int, movieName: String)

  case class Movie(id: Int, movieName: String, MPAA: Option[String], genre: Option[String], runTime: Option[Int], budget: Option[Int], studio: Option[String])

  case class Daily(date: DateTime, gross: Int, movie: Movie, regionName: String, theaters: Int)

  case class Release(date: DateTime, movieId: Int, regionName: String)

}




