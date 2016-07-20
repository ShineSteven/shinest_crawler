package shine.st.crawler.tmp


import javax.naming.directory.SearchResult

import com.sksamuel.elastic4s.ElasticDsl._
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory
import shine.st.crawler.{MovieSelector, WebCrawler}
import shine.st.crawler.model.CrawlerModel.{DailyInfo, MovieInfo}
import shine.st.crawler.model.MovieModel.{Daily, Movie}
import shine.st.crawler.search.SearchUtils

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
/**
  * Created by stevenfanchiang on 2016/6/22.
  */
object Main {
  val config = ConfigFactory.load
  val log = LoggerFactory.getLogger(Main.getClass)

  def main(args: Array[String]): Unit = {
//    val movieInfo = WebCrawler.query("http://www.boxofficemojo.com/movies/?page=daily&id=freestateofjones.htm", Map.empty[String, String])(MovieSelector.movie)
//    val dailyInfo = WebCrawler.query("http://www.boxofficemojo.com/daily/chart/?view=1day&sortdate=2016-06-28&p=.htm", Map.empty[String, String])(MovieSelector.daily)
//
//    log.debug(movieInfo.toString)
//    log.debug(dailyInfo.toString)
////
//    val d = trans(dailyInfo.get.head)
//    val m = trans(movieInfo.get)
//
//    SearchUtils.createIndex("movie_predict", "daily", d.movie.id + d.date.hashCode(), d)
//
//    SearchUtils.createIndex("movie_predict", "movie", m.id, m)

    val client = SearchUtils.client
    val result = client.execute {
      search in "movie_predict" / "movie"  query termQuery("movie_name.raw","Free State of Jones")
    }

//    result.onSuccess{case s:SearchResult => println(s)}
    val response = Await.result(result, 5 seconds)
    println(response.totalHits)
  }

  def trans(d: DailyInfo) = {
    Daily(d.date, d.gross, Movie(d.movieInfo.title.hashCode, d.movieInfo.title, None, None, None, None, None), d.regionName, d.theaters)
  }

  def trans(m: MovieInfo) = {
    Movie(m.movieName.hashCode, m.movieName, Option(m.MPAA), Option(m.genre), Option(m.runTime), Option(m.budget), Option(m.studio))
  }

}
