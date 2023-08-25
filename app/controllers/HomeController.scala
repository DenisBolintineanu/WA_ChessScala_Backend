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

  def doMove(id: String, playerID: String, move: String): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    persistenceService.updateGame(move, id, playerID, asJson = false) match {
      case Some(board) => Ok(board)
      case _ => Ok(ERROR_RESPONSE)
    }
  }

  def newGame(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(persistenceService.createGame())
  }

  def updateGame(id: String): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    persistenceService.readGame(id, asJson = false) match {
      case Some(board) => Ok(board)
      case _ => Ok(ERROR_RESPONSE)
    }
  }
}
