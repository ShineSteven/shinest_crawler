package shine.st.crawler.tmp

import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, JsValue, Json, Writes}

/**
  * Created by shinest on 2016/7/9.
  */
object JsonPrac {

  def main(args: Array[String]): Unit = {

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
//      None,
      Location(51.235685, -1.309197),
//      null,
      Seq(
        Resident("Fiver", 4, None),
        Resident("Bigwig", 6, Some("Owsla"))
      )
    )
    val json2 = Json.toJson(place)
    println(json2)

    val tmp: _root_.play.api.libs.functional.FunctionalBuilder[_root_.play.api.libs.json.OWrites]#CanBuild2[Double, Double] = ((JsPath \ "lat").write[Double] and
      (JsPath \ "long").write[Double])

  }

  case class Location(lat: Double, long: Double)

  case class Resident(name: String, age: Int, role: Option[String])

  case class Place(name: String, location: Location, residents: Seq[Resident])


  implicit val locationWrites: Writes[Location] = (
    (JsPath \ "lat").write[Double] and
      (JsPath \ "long").write[Double]
    ) (unlift(Location.unapply))

  implicit val residentWrites: Writes[Resident] = (
    (JsPath \ "name").write[String] and
      (JsPath \ "age").write[Int] and
      (JsPath \ "role").writeNullable[String]
    ) (unlift(Resident.unapply))

  implicit val placeWrites: Writes[Place] = (
    (JsPath \ "name").write[String] and
      (JsPath \ "location").write[Location] and
      (JsPath \ "residents").write[Seq[Resident]]
    ) (unlift(Place.unapply))
}
