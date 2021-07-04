package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import models.Cars
import scala.concurrent.{ExecutionContext, Future}
import repositories.CarsRepository
import reactivemongo.bson.BSONObjectID
import play.api.libs.json.{Json, __}
import play.api.libs.json.JsValue
import scala.util.{Failure, Success}


@Singleton
class HomeController @Inject()(
    implicit executionContext: ExecutionContext,
    val carsRepository: CarsRepository,
    val controllerComponents: ControllerComponents) 
  extends BaseController {

  
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok("App works")
  }

  def findAll():Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
  carsRepository.findAll().map {
    caradverts => Ok(Json.toJson(caradverts))
  }
  
 }

 def findOne(id:String):Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
  val objectIdTryResult = BSONObjectID.parse(id)
  objectIdTryResult match {
    case Success(objectId) => carsRepository.findOne(objectId).map {
      caradverts => Ok(Json.toJson(caradverts))
    }
    case Failure(_) => Future.successful(BadRequest("Cannot parse the caradvert id"))
  }
}

def create():Action[JsValue] = Action.async(controllerComponents.parsers.json) { implicit request => {

   request.body.validate[Cars].fold(
     _ => Future.successful(BadRequest("Cannot parse request body")),
     caradverts =>
       carsRepository.create(caradverts).map {
         _ => Created(Json.toJson(caradverts))
     }
   )
}}

def updateOne(id: String):Action[JsValue] = Action.async(controllerComponents.parsers.json) { implicit request => {
  request.body.validate[Cars].fold(
    _ => Future.successful(BadRequest("Cannot parse request body")),
    car =>{
      val objectIdTryResult = BSONObjectID.parse(id)
      objectIdTryResult match {
        case Success(objectId) => carsRepository.updateOne(objectId, car).map {
          result => Ok(Json.toJson(result.ok))
        }
        case Failure(_) => Future.successful(BadRequest("Cannot parse the car id"))
      }
    }
  )
 }
}

def delete(id: String):Action[AnyContent]  = Action.async { implicit request => {
  val objectIdTryResult = BSONObjectID.parse(id)
  objectIdTryResult match {
    case Success(objectId) => carsRepository.delete(objectId).map {
      _ => NoContent
    }
    case Failure(_) => Future.successful(BadRequest("Cannot parse the car id"))
  }
}}
}
