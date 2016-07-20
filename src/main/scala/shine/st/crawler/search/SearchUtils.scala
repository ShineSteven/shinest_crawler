package shine.st.crawler.search

import com.sksamuel.elastic4s.ElasticDsl.{index, _}
import com.sksamuel.elastic4s.{ElasticClient, QueryDefinition}
import play.api.libs.json.{Json, Writes}

/**
  * Created by stevenfanchiang on 2016/7/7.
  */
object SearchUtils {
  val client = ElasticClient.remote("localhost", 9300)


  def createIndex[D](indexName: String, typeName: String, id: Int, data: D)(implicit write: Writes[D]) = {
    client.execute {
      index into indexName / typeName id id source Json.toJson(data).toString()
    }
  }

  def query(indexName: String, typeName: String,block: => QueryDefinition) = {
    client.execute {
      search in indexName / typeName query block
    }
  }
}
