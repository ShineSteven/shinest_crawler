package shine.st.crawler.data.index

import org.joda.time.DateTime
import shine.st.common.HashUtils

/**
  * Created by shinest on 2016/7/2.
  */
object BoxOffice {

  trait Document[S] {
    def source: Option[S]

    def id: String
  }

  trait Parent[S, A <: Document[_]] extends Document[S] {
    def parent: A

    def parentId: String = parent.id
  }

  case class MovieSource(name: String, MPAA: String, genre: String, duration: Int, budget: String, studio: String, release: List[Release], updateAt: Option[DateTime] = None)

  case class DailySource(date: DateTime, gross: Int, rank: Int, theaters: Int, regionName: String, studio: String, updateAt: Option[DateTime] = None)

  case class WeeklySource(year: Int, week: Int, onReleaseWeek: Int, weeklyGross: Int, weekdayGross: Int, weekendGross: Int, rank: Int, theaters: Int, regionName: String, studio: String, updateAt: Option[DateTime] = None)


  case class Release(regionName: String, releaseDate: DateTime)

  case class Movie(source: Option[MovieSource], id: String = HashUtils.uuidBase64) extends Document[MovieSource]

  case class Daily(source: Option[DailySource], parent: Movie, id: String = HashUtils.uuidBase64) extends Parent[DailySource, Movie]

  case class Weekly(source: Option[WeeklySource], parent: Movie, id: String = HashUtils.uuidBase64) extends Parent[WeeklySource, Movie]

}




