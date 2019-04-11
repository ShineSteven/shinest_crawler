package shine.st.crawler.actor

import akka.actor.{Props, Terminated}
import akka.routing.{ActorRefRoutee, RoundRobinRoutingLogic, Router}
import org.joda.time.DateTime
import shine.st.crawler.model.Message._

import scala.concurrent.duration._

class MasterActor extends BaseActor {
  var router: Router = _

  override def adapterReceive: Receive = {
    case Start(executorActorAmount) =>
      val movieActors = (1 to executorActorAmount).map { id =>
        val actor = context.actorOf(MovieActor.props, s"movie-actor-$id")
        context watch actor
        ActorRefRoutee(actor)
      }.toVector

      router = Router(RoundRobinRoutingLogic(), movieActors)

      val actorSystem = context.system
      import actorSystem._
      actorSystem.scheduler.schedule(1 minute, 1 day, self, new QueryDate(new DateTime()))
    //      dateList.foreach { date => queryActor ! Message.Daily(date) }


    case qd: QueryDate =>
      router.route(qd, self)

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
  def apply(): Props = Props(classOf[MasterActor])

  def props(): Props = Props[WebQueryActor]

}