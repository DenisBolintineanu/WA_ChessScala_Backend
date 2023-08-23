package controllers

import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents, Request}
import services.IPersistenceService
import utils.DefaultServerResponses.ERROR_RESPONSE

import javax.inject.{Inject, Singleton}

@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents, val persistenceService: IPersistenceService) extends BaseController {

  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok("WELCOME TO CHESS PLAY")
  }

  def playChess2(id: String): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val parameter1 = request.body.asFormUrlEncoded.get("move").headOption.getOrElse("")
    persistenceService.updateGame(parameter1, id, asJson = false) match {
      case Some(board) => Ok(board)
      case _ => Ok(ERROR_RESPONSE)
    }
  }

  def newGame(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Redirect(routes.HomeController.joinGame(persistenceService.createGame()))
  }

  def joinGame(id: String): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    persistenceService.readGame(id, asJson = false) match {
      case Some(board) => Ok(board)
      case _ => Ok(ERROR_RESPONSE)
    }
  }

}
