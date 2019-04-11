package shine.st.crawler.actor

import akka.actor.Props
import com.sksamuel.elastic4s.ElasticsearchClientUri
import com.sksamuel.elastic4s.http.ElasticDsl.{indexInto, _}
import com.sksamuel.elastic4s.http.HttpClient
import io.circe.syntax._
import shine.st.crawler.model.Message.{End, Flush}
import shine.st.crawler.data._
import shine.st.crawler.data.index.BoxOffice.{Daily, Movie, Weekly}
import shine.st.crawler.search.ElasticsearchUtils

import scala.collection.mutable.ListBuffer

/**
  * Created by shinest on 2016/7/16.
  */
class ElasticsearchActor extends BaseActor {

  val client = HttpClient(ElasticsearchClientUri("localhost", 9200))
  val dailyBuffer = new ListBuffer[Daily]()
  val weeklyBuffer = new ListBuffer[Weekly]()


  override def adapterReceive: Receive = {

    case d: Daily =>
      dailyBuffer += d

      if (dailyBuffer.size > 50) {
        ElasticsearchUtils.bulkInsert(dailyBuffer.toList.map(daily => {
          indexInto("box_office" / "daily").parent(daily.parentId).id(daily.id).source(daily.source.asJson.noSpaces)
        }))

        dailyBuffer.clear
      }

    //      sender ! Complete(1)

    case w: Weekly =>
      weeklyBuffer += w

      if (weeklyBuffer.size > 50) {
        ElasticsearchUtils.bulkInsert(weeklyBuffer.toList.map(weekly => {
          indexInto("box_office" / "weekly").parent(weekly.parentId).id(weekly.id).source(weekly.source.asJson.noSpaces)
        }))


        weeklyBuffer.clear
      }

    //      sender ! Complete(1)

    case m: Movie =>
      ElasticsearchUtils.execute {
        indexInto("box_office" / "movie") id m.id source (
          m.source.asJson.noSpaces
          )
      }

    case Flush =>
      if (dailyBuffer.size > 0) {
        ElasticsearchUtils.bulkInsert(dailyBuffer.toList.map(daily => {
          indexInto("box_office" / "daily").parent(daily.parentId).id(daily.id).source(daily.source.asJson.noSpaces)
        }))
      }

      if (weeklyBuffer.size > 0) {
        ElasticsearchUtils.bulkInsert(weeklyBuffer.toList.map(weekly => {
          indexInto("box_office" / "weekly").parent(weekly.parentId).id(weekly.id).source(weekly.source.asJson.noSpaces)
        }))
      }

      self ! End

  }
}

object ElasticsearchActor {
  def props(): Props = Props[ElasticsearchActor]
}
