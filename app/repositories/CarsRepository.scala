package repositories

import javax.inject._
import reactivemongo.api.bson.collection.BSONCollection
import play.modules.reactivemongo.ReactiveMongoApi
import scala.concurrent.{ExecutionContext, Future}
import models.Cars
import reactivemongo.api.{Cursor, ReadPreference}
import reactivemongo.bson.{BSONDocument, BSONObjectID}

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
}