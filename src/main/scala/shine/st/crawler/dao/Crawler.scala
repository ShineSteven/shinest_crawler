package shine.st.crawler

import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.{CloseableHttpClient, HttpClients}
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

import scala.util.control.NonFatal

/**
  * Created by shinest on 2016/7/2.
  */


//trait Crawler[T] {
//  def get(uri: String): T
//}

trait Crawler[D] {
  def get(uri: String): D

  def query[T](uri: String, param: Map[String, String])(parse: D => T): Option[T] = {
    val fullUri = if (param.isEmpty)
      uri
    else
      s"$uri?${param.map { case (k, v) => s"$k=$v" }.mkString("&")}"

    try {
      Option(parse(get(fullUri)))
    } catch {
      case NonFatal(e) =>
        e.printStackTrace()
        None
    }
  }

  def query[T](uri: String)(parse: D => T): Option[T] = {
    query(uri,Map.empty[String,String])(parse)
  }
}

object ApiCrawler extends Crawler[HttpResponse] {
  override def get(uri: String) = {
    val httpclient: CloseableHttpClient = HttpClients.createDefault()
    val httpGet: HttpGet = new HttpGet(uri)
    httpclient.execute(httpGet)
  }
}

object WebCrawler extends Crawler[Document] {
  override def get(uri: String) = {
    Jsoup.connect(uri).get
  }
}


