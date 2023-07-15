package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import ChessScala.controller.IController
import ChessScala.controller.Controller

import scala.collection.mutable

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */

  private var controllerMapping: Map[String, IController] = Map.empty

  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val gameID = java.util.UUID.randomUUID().toString
    val controller = new Controller()
    controllerMapping = controllerMapping + (gameID -> controller)
    Redirect(f"/game?id=$gameID")
  }

  def playChess2(id: String): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val parameter1 = request.body.asFormUrlEncoded.get("command").headOption.getOrElse("")
    val controller = controllerMapping(id)
    controller.computeInput(parameter1)
    val controllerAsText: String = controller.output
    Ok(views.html.chess(controllerAsText, id))
  }

  def joinGame(id: String): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val gameID = id
    val controller = controllerMapping(gameID)
    val controllerAsText: String = controller.output
    Ok(views.html.chess(controllerAsText, gameID))
  }

}
