package models

import play.api.libs.json.{Format, Json}
import reactivemongo.play.json._
import reactivemongo.bson.BSONObjectID
import reactivemongo.bson._



case class Cars(
                  _id:Option[BSONObjectID],
                  title:String,
                  fuelType:String,
                  price:Int,
                  isNew:Boolean,
                  mileage:Int,
                  firstRegistration:String
                )
object Cars{
  implicit val fmt : Format[Cars] = Json.format[Cars]
  implicit object CarsBSONReader extends BSONDocumentReader[Cars] {
    def read(doc: BSONDocument): Cars = {
      Cars(
        doc.getAs[BSONObjectID]("_id"),
        doc.getAs[String]("title").get,
        doc.getAs[String]("fuelType").get,
        doc.getAs[Int]("price").get,
        doc.getAs[Boolean]("isNew").get,
        doc.getAs[Int]("mileage").get,
        doc.getAs[String]("firstRegistration").get
        )
    }
  }

  implicit object CarsBSONWriter extends BSONDocumentWriter[Cars] {
    def write(cars: Cars): BSONDocument = {
      BSONDocument(
        "_id" -> cars._id,
        "title" -> cars.title,
        "fuelType" -> cars.fuelType,
        "price" -> cars.price,
        "isNew" -> cars.isNew,
        "mileage" -> cars.mileage,
        "firstRegistration" -> cars.firstRegistration
      )
    }
  }
}