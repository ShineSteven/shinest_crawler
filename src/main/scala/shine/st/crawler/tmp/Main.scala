package shine.st.crawler.tmp


import org.slf4j.LoggerFactory
import play.api.libs.functional.syntax._
import play.api.libs.json._
import shine.st.crawler.{MovieSelector, WebCrawler}

/**
  * Created by stevenfanchiang on 2016/6/22.
  */
object Main {
  val log = LoggerFactory.getLogger(Main.getClass)

  def main(args: Array[String]): Unit = {
//    val movieInfo = WebCrawler.query("http://www.boxofficemojo.com/movies/?page=daily&id=freestateofjones.htm", Map.empty[String, String])(MovieSelector.movie)
//    val dailyInfo = WebCrawler.query("http://www.boxofficemojo.com/daily/chart/?view=1day&sortdate=2016-06-28&p=.htm", Map.empty[String, String])(MovieSelector.daily)
//
//    log.debug(movieInfo.toString)
//    log.debug(dailyInfo.toString)

    val json: JsValue = Json.parse(
      """
{
  "name" : "Watership Down",
  "location" : {
    "lat" : 51.235685,
    "long" : -1.309197
  },
  "residents" : [ {
    "name" : "Fiver",
    "age" : 4,
    "role" : null
  }, {
    "name" : "Bigwig",
    "age" : 6,
    "role" : "Owsla"
  } ]
}
      """)





    val place = Place(
      "Watership Down",
      Location(51.235685, -1.309197),
      Seq(
        Resident("Fiver", 4, None),
        Resident("Bigwig", 6, Some("Owsla"))
      )
    )
    val json2 = Json.toJson(place)
    println(json2)

  }

  case class Location(lat: Double, long: Double)
  case class Resident(name: String, age: Int, role: Option[String])
  case class Place(name: String, location: Location, residents: Seq[Resident])


  implicit val locationWrites: Writes[Location] = (
    (JsPath \ "lat").write[Double] and
      (JsPath \ "long").write[Double]
    )(unlift(Location.unapply))

  implicit val residentWrites: Writes[Resident] = (
    (JsPath \ "name").write[String] and
      (JsPath \ "age").write[Int] and
      (JsPath \ "role").writeNullable[String]
    )(unlift(Resident.unapply))

  implicit val placeWrites: Writes[Place] = (
    (JsPath \ "name").write[String] and
      (JsPath \ "location").write[Location] and
      (JsPath \ "residents").write[Seq[Resident]]
    )(unlift(Place.unapply))
}
