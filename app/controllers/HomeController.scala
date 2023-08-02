package controllers

import play.api.mvc._
import utils.CleanUpTask

import javax.inject._

@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents, val cleanUpTask: CleanUpTask) extends BaseController {

  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def playChess2(id: String): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    if (!cleanUpTask.controllerMapping.contains(id)) {
      Ok(views.html.gameNotFound(id))
    } else {
      val parameter1 = request.body.asFormUrlEncoded.get("command").headOption.getOrElse("")
      val controller = cleanUpTask.controllerMapping(id)
      cleanUpTask.garbageCollector(id) = true
      controller.computeInput(parameter1)
      val controllerAsText: String = controller.output
      Ok(views.html.chess(controllerAsText, id, controller.returnMoveList()))
    }
  }

  def newGame(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Redirect(routes.HomeController.joinGame(cleanUpTask.gameSetup))
  }

  def joinGame(id: String): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    if (!cleanUpTask.controllerMapping.contains(id)) {
      Ok(views.html.gameNotFound(id))
    } else {
      val controller = cleanUpTask.controllerMapping(id)
      val controllerAsText: String = controller.output
      Ok(views.html.chess(controllerAsText, id, controller.returnMoveList()))
    }
  }

  def adminPage(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.admin(Map.from(cleanUpTask.garbageCollector)))
  }

}
