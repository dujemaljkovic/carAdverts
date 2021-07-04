package repositories

import javax.inject._
import reactivemongo.api.bson.collection.BSONCollection
import play.modules.reactivemongo.ReactiveMongoApi
import scala.concurrent.{ExecutionContext, Future}
import models.Cars
import reactivemongo.api.{Cursor, ReadPreference}
import reactivemongo.bson.{BSONDocument, BSONObjectID}
import reactivemongo.api.commands.WriteResult

@Singleton
class CarsRepository @Inject()(
    implicit executionContext: ExecutionContext,
    reactiveMongoApi: ReactiveMongoApi
) {
    def collection: Future[BSONCollection] = reactiveMongoApi.database.map(db => db.collection("caradverts"))
        
    def findAll(limit: Int = 100): Future[Seq[Cars]] = {

        collection.flatMap(
            _.find(BSONDocument(), Option.empty[Cars])
            .cursor[Cars](ReadPreference.Primary)
            .collect[Seq](limit, Cursor.FailOnError[Seq[Cars]]())
         )
     }
     

     def findOne(id: BSONObjectID): Future[Option[Cars]] = {
     collection.flatMap(_.find(BSONDocument("_id" -> id), Option.empty[Cars]).one[Cars])
    }

   
    def create(cars: Cars): Future[WriteResult] = {
    collection.flatMap(_.insert(ordered = false)
    .one(cars.copy()))
    
    }

    def updateOne(id: BSONObjectID, cars: Cars):Future[WriteResult] = {

    collection.flatMap(
    _.update(ordered = false).one(BSONDocument("_id" -> id),
      cars.copy()))
 }

    def delete(id: BSONObjectID):Future[WriteResult] = {
    collection.flatMap(
        _.delete().one(BSONDocument("_id" -> id), Some(1))
    )
    }
}
