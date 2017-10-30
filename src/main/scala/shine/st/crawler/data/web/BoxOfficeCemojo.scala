package shine.st.crawler.data.web

import org.joda.time.DateTime

/**
  * Created by shinest on 2016/7/7.
  */
object BoxOfficeCemojo {

  case class Movie(name: String, MPAA: String, genre: String, duration: Int, budget: String, studio: Link, usReleaseDate: DateTime)

  case class Daily(rank: Int, date: DateTime, movieInfo: Link, studio: Link, gross: Int, theaters: Int)

  case class Weekly(year: Int, week: Int, rank: Int, onReleaseWeek: Int, weeklyGross: Int, weekdayGross: Int, weekendGross: Int, theaters: Int, movieInfo: Link, studio: Link)

  case class Link(title: String, link: String)

}




