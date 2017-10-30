package shine.st.crawler.actor

import akka.actor.Actor
import akka.pattern.gracefulStop
import shine.st.crawler.Dump
import shine.st.crawler.actor.Message.End

import scala.concurrent.duration._

/**
  * Created by shinest on 2016/7/19.
  */
abstract class CommonActor extends Actor {
  def endReceive: Receive = {
    case End =>
      gracefulStop(self, 5 seconds)

    case a => Dump.logger.error(s"error ${a.toString}")
  }

  override def receive = realReceive orElse endReceive

  def realReceive: Receive
}