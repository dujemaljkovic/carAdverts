package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}
import repositories.CarsRepository
import reactivemongo.bson.BSONObjectID
import play.api.libs.json.{Json, __}
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
}
