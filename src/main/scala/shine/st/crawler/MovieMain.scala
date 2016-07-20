package shine.st.crawler

import akka.actor.ActorSystem
import org.joda.time.DateTime
import shine.st.common.DateTimeUtils
import shine.st.common.akka.Message.Check
import shine.st.crawler.actor.{BigBoss, MovieCrawlerActor}
import shine.st.crawler.actor.MovieCrawlerActor.CrawlerDate

/**
  * Created by stevenfanchiang on 2016/7/19.
  */
object MovieMain {
  def main(args: Array[String]): Unit = {
    Dump.logger.debug("movie query main start...")
    val system = ActorSystem("CrawlerMovie")
    val movieActor = system.actorOf(MovieCrawlerActor.props, "movieActor")
    val bigBossActor = system.actorOf(BigBoss(movieActor), "bigBossActor")

    val runDate = new DateTime().minusDays(3)
    Dump.logger.debug(s"run date ${runDate.toString(DateTimeUtils.dateTimeFormat)}")

    movieActor ! CrawlerDate(List(runDate))
    bigBossActor ! Check

    system.awaitTermination()




  }
}
