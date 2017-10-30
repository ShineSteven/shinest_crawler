package shine.st.crawler

import org.joda.time.DateTime
import shine.st.common.DateTimeUtils
import shine.st.crawler.data.index.BoxOffice
import shine.st.crawler.data.index.BoxOffice.Release
//import spray.json.{DefaultJsonProtocol, JsObject, JsValue, _}


package object data {
  //  package object data extends DefaultJsonProtocol {
  //
  //  implicit object DateJsonFormat extends RootJsonFormat[DateTime] {
  //
  //    override def write(date: DateTime) = JsObject(("date", JsString(DateTimeUtils.formatISO(date))))
  //
  //    override def read(json: JsValue): DateTime = {
  //      json match {
  //        case j: JsObject =>
  //          DateTimeUtils.parseISO(j.getFields("date").head.convertTo[String]).get //parserISO.parseDateTime(s)
  //        case _ => throw new DeserializationException("Error info you want here ...")
  //      }
  //    }
  //  }
  //
  //
  //  //  elasticsearch box_office index
  //  implicit val BOReleaseJsonFormat = jsonFormat2(BoxOffice.Release)
  //
  //  implicit val BOMovieSourceJsonFormat = jsonFormat(BoxOffice.MovieSource, "name", "MPAA", "genre", "duration", "budget", "studio", "update_at", "release")
  //
  //  implicit val BODailySourceJsonFormat = jsonFormat(BoxOffice.DailySource, "date", "gross", "rank", "theaters", "region_name", "studio", "update_at")
  //
  //  implicit val BOWeeklySourceJsonFormat = jsonFormat(BoxOffice.WeeklySource, "week", "on_release_week", "weekly_gross", "weekday_gorss", "weekend_gross", "rank", "theaters", "region_name", "studio", "update_at")

  import io.circe._
  import io.circe.generic.semiauto._
  import io.circe.syntax._
  //  case class Foo(a: Int, b: String, c: Boolean,d:DateTime)
  //
  //  implicit val fooDecoder: Decoder[Foo] = deriveDecoder[Foo]
  //  implicit val fooEncoder: Encoder[Foo] = deriveEncoder[Foo]

  implicit val dateTimeEncoder: Encoder[DateTime] = Encoder.instance(date => DateTimeUtils.formatISO(date).asJson)
  implicit val dateTimeDecoder: Decoder[DateTime] = Decoder.instance(a => a.as[String].map(ds => DateTimeUtils.parseISO(ds).get))


  implicit val releaseEncoder: Encoder[BoxOffice.Release] = Encoder.forProduct2("region_name", "release_date")(r => (r.regionName, r.releaseDate))
  implicit val releaseDecoder: Decoder[BoxOffice.Release] = Decoder.forProduct2("region_name", "release_date")(BoxOffice.Release.apply)

  implicit val movieSourceEncoder: Encoder[BoxOffice.MovieSource] = Encoder.forProduct8("name", "MPAA", "genre", "duration", "budget", "studio", "release", "update_at")(m => (m.name, m.MPAA, m.genre, m.duration, m.budget, m.studio, m.release, m.updateAt))
  implicit val movieSourceDecoder: Decoder[BoxOffice.MovieSource] = Decoder.forProduct8("name", "MPAA", "genre", "duration", "budget", "studio", "release", "update_at")(BoxOffice.MovieSource.apply)

  implicit val dailySourceEncoder: Encoder[BoxOffice.DailySource] = Encoder.forProduct7("date", "gross", "rank", "theaters", "region_name", "studio", "update_at")(d => (d.date, d.gross, d.rank, d.theaters, d.regionName, d.studio, d.updateAt))
  implicit val dailySourceDecoder: Decoder[BoxOffice.DailySource] = Decoder.forProduct7("date", "gross", "rank", "theaters", "region_name", "studio", "update_at")(BoxOffice.DailySource.apply)

  implicit val weeklySourceEncoder: Encoder[BoxOffice.WeeklySource] = Encoder.forProduct11("year", "week", "on_release_week", "weekly_gross", "weekday_gross", "weekend_gross", "rank", "theaters", "region_name", "studio", "update_at")(w => (w.year, w.week, w.onReleaseWeek, w.weeklyGross, w.weekdayGross, w.weekendGross, w.rank, w.theaters, w.regionName, w.studio, w.updateAt))
  implicit val weeklySourceDecoder: Decoder[BoxOffice.WeeklySource] = Decoder.forProduct11("year", "week", "on_release_week", "weekly_gross", "weekday_gross", "weekend_gross", "rank", "theaters", "region_name", "studio", "update_at")(BoxOffice.WeeklySource.apply)

}
