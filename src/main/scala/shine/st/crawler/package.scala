package shine.st

import org.joda.time.DateTime
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, JsString, JsValue, Writes}
import shine.st.common.DateTimeUtils
import shine.st.crawler.model.MovieModel.{Daily, Movie, Release}
import scala.concurrent.duration._
/**
  * Created by shinest on 2016/7/12.
  */
package object crawler {
  implicit val defaultTimeout = 5 seconds

  implicit val dateWriter: Writes[DateTime] = new Writes[DateTime] {
    override def writes(o: DateTime): JsValue = JsString(o.toString(DateTimeUtils.dateTimeFormat))
  }

  implicit val movieWrites: Writes[Movie] = (
//    (JsPath \ "id").writeNullable[Int] and
      (JsPath \ "id").write[Int] and
      (JsPath \ "movie_name").write[String] and
      (JsPath \ "MPAA").writeNullable[String] and
      (JsPath \ "genre").writeNullable[String] and
      (JsPath \ "run_time").writeNullable[Int] and
      (JsPath \ "budge").writeNullable[Int] and
      (JsPath \ "studio").writeNullable[String]
    )(unlift(Movie.unapply))

  implicit val dailyWrites: Writes[Daily] = (
    (JsPath \ "date").write[DateTime] and
      (JsPath \ "gross").write[Int] and
      (JsPath \ "movie").write[Movie] and
      (JsPath \ "region_name").write[String] and
      (JsPath \ "theaters").write[Int]
    )(unlift(Daily.unapply))

  implicit val releaseWrites: Writes[Release] = (
    (JsPath \ "date").write[DateTime] and
      (JsPath \ "movie_id").write[Int] and
      (JsPath \ "region_name").write[String]
    )(unlift(Release.unapply))
}
