package shine.st.crawler.model

import org.joda.time.DateTime

/**
  * Created by stevenfanchiang on 2016/7/7.
  */
object CrawlerModel {

  case class MovieInfo(movieName: String, MPAA: String, genre: String, runTime: Int, budget: Int, studio: String)

  case class DailyInfo(date: DateTime, movieInfo: Link, studio: Link, gross: Int, theaters: Int)

  case class Link(title: String, link: String)

}
