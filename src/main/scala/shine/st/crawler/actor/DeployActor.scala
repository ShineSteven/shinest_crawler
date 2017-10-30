package shine.st.crawler.actor

import akka.actor.Props
import shine.st.crawler.actor.Message.{Deploy, Remote}

class DeployActor extends CommonActor {


  override def realReceive: Receive = {
    case Deploy(count) =>
      val actors = (1 to count).map(id => context.actorOf(MovieActor.props, s"movie-actor-$id")).toVector

      sender ! Remote(actors)

  }
}

object DeployActor {
  def props(): Props = Props[DeployActor]
}