package shine.st.crawler.actor

import akka.actor.Actor
import akka.pattern.gracefulStop
import shine.st.common.akka.Message.{End, EndComplete}

import scala.concurrent.duration._

/**
  * Created by stevenfanchiang on 2016/7/19.
  */
abstract class CommonActor extends Actor {
  def endReceive: Receive = {
    case End =>
      gracefulStop(self, 5 seconds)
      sender ! EndComplete
  }

  override def receive = realReceive orElse endReceive

  def realReceive: Receive
}