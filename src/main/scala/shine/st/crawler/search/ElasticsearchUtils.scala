package shine.st.crawler.search

import com.sksamuel.elastic4s.ElasticsearchClientUri
import com.sksamuel.elastic4s.http.ElasticDsl._
import com.sksamuel.elastic4s.http.{HttpClient, HttpExecutable}
import com.sksamuel.elastic4s.indexes.IndexDefinition

/**
  * Created by shinest on 2016/7/7.
  */
object ElasticsearchUtils {


  def execute[T, U](request: T)(implicit exec: HttpExecutable[T, U]) = {
    val client = HttpClient(ElasticsearchClientUri("localhost", 9200))
    val r = client.execute(request).await
    client.close
    r
  }


  def termQuery(indexName: String, typeName: String, field: String, value: String) = {
    execute {
      search(indexName / typeName) termQuery(field, value)
    }
  }

  def bulkInsert(data: List[IndexDefinition]) = {
    execute {
      bulk(
        data: _*
      )
    }
  }
}
