package shine.st.crawler

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import org.joda.time.{DateTime, DateTimeConstants}
import shine.st.common.{DateTimeUtils, NumberUtils}
import shine.st.crawler.Dump.logger
import shine.st.crawler.actor.MasterActor
import shine.st.crawler.model.Message.{QueryDate, QueryWeek, Start}

import scala.concurrent.Await
import scala.concurrent.duration._


object BoxOfficeCrawler {
  val defaultDate = new DateTime().minusDays(3).secondOfDay().withMinimumValue()
  val config = ConfigFactory.load

  def main(args: Array[String]): Unit = {
    logger.info("box office crawler process start...")

    //    val now = DateTimeUtils.now
    //    val defaultDate = now.minusDays(2)
    //    val defaultWeek = movieWeek(now.minusDays(9))

    val movieExecutorAmount = config.getInt("movie.execute.actor.amount")
    val system = ActorSystem("BoxOfficeCrawler")
    import system._

    val masterActor = system.actorOf(MasterActor.props(), "master-actor")
    masterActor ! Start(movieExecutorAmount)

    Await.result(system.whenTerminated.map(_ => logger.info("Actor system was shut down")), Duration.Inf)
  }

  def parseDateOrDefault(text: String) = {
    DateTimeUtils.parseDate(text).getOrElse(defaultDate)
  }

}
