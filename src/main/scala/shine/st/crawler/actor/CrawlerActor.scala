package shine.st.crawler.actor

import akka.actor.{Props, Terminated}
import akka.routing.{ActorRefRoutee, RoundRobinRoutingLogic, Router}
import shine.st.crawler.actor.Message._

/**
  * Created by shinest on 2016/7/12.
  */

class MasterActor(dailyList: List[QueryDate], weeklyList: List[QueryWeek]) extends CommonActor {
  val deployActor = context.actorOf(DeployActor.props, "deploy-actor")
  var router: Router = _

  override def realReceive: Receive = {
    case Start =>
      deployActor ! Deploy(5)
    //      dateList.foreach { date => queryActor ! Message.Daily(date) }

    case Remote(actors) =>
      router = Router(RoundRobinRoutingLogic(), actors.map { r =>
        context watch r
        ActorRefRoutee(r)
      })

      dailyList.foreach(d => router.route(d, sender()))
      weeklyList.foreach(w => router.route(w, sender()))

    case Terminated(a) =>
      router = router.removeRoutee(a)
      if (router.routees.size == 0) {
        self ! End
        context.system.terminate()
      }
    //      val r = context.actorOf(Props[Worker])
    //      context watch r
    //      router = router.addRoutee(r)
  }

}

object MasterActor {
  def apply(dailyList: List[QueryDate], weeklyList: List[QueryWeek]): Props = Props(classOf[MasterActor], dailyList, weeklyList)

}