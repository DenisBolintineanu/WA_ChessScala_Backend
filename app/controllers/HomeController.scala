package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import ChessScala.controller.IController
import ChessScala.controller.Controller

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

    var controller: IController = new Controller()


  def index() = Action { implicit request: Request[AnyContent] =>
    controller = new Controller()
    Ok(views.html.chess(""))
  }

  def playChess(command: String): Action[AnyContent] = Action {
    controller.computeInput(command)
    val controllerAsText: String = controller.output
    Ok(views.html.chess(controllerAsText))
  }

  def playChess2() = Action { implicit request: Request[AnyContent] =>
    val parameter1 = request.body.asFormUrlEncoded.get("command").headOption.getOrElse("")
    controller.computeInput(parameter1)
    val controllerAsText: String = controller.output
    Ok(views.html.chess(controllerAsText))
  }

}
