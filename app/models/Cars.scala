package models

import play.api.libs.json.{Format, Json}
import reactivemongo.play.json._
import reactivemongo.bson.BSONObjectID
import reactivemongo.bson._



case class Cars(
                  _id:Option[BSONObjectID]
                  title:String,
                  description:String
                )
object Cars{
  implicit val fmt : Format[Cars] = Json.format[Cars]
  implicit object MovieBSONReader extends BSONDocumentReader[Cars] {
    def read(doc: BSONDocument): Cars = {
      Cars(
        doc.getAs[BSONObjectID]("_id"),
        doc.getAs[String]("title").get,
        doc.getAs[String]("description").get)
    }
  }

  implicit object MovieBSONWriter extends BSONDocumentWriter[Cars] {
    def write(cars: Cars): BSONDocument = {
      BSONDocument(
        "_id" -> cars._id,
        "title" -> cars.title,
        "description" -> cars.description

      )
    }
  }
}