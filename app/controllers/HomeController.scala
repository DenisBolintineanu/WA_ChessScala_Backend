package controllers

import play.api.mvc._
import utils.CleanUpTask

import javax.inject._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents, val cleanUpTask: CleanUpTask) extends BaseController {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */

  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index(cleanUpTask.gameSetup))
  }

  def playChess2(id: String): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val parameter1 = request.body.asFormUrlEncoded.get("command").headOption.getOrElse("")
    val controller = cleanUpTask.controllerMapping(id)
    synchronized {
      cleanUpTask.garbageCollector = cleanUpTask.garbageCollector.map(x => if (x._1 == id) (x._1, true) else x)
    }
    controller.computeInput(parameter1)
    val controllerAsText: String = controller.output
    Ok(views.html.chess(controllerAsText, id, controller.returnMoveList()))
  }

  def joinGame(id: String): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val gameID = id
    val controller = cleanUpTask.controllerMapping(gameID)
    val controllerAsText: String = controller.output
    Ok(views.html.chess(controllerAsText, gameID, controller.returnMoveList()))
  }

  def adminPage(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.admin(cleanUpTask.garbageCollector))
  }

}
