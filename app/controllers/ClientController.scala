package controllers

import ChessScala.controller.Controller
import play.api.Configuration
import play.api.mvc._
import services.IGameService
import services.db.MongoDBService
import services.local.LocalPersistenceService

import javax.inject._

@Singleton
class ClientController @Inject()(val controllerComponents: ControllerComponents, val persistenceService: LocalPersistenceService) extends BaseController {

  private val MOVE_ID: String = "move"
  private val GAME_ID: String = "id"
  private val SUCCESS_RESPONSE: String = "SUCCESS"
  private val INVALID_RESPONSE: String = "invalid"
  private val ERROR_RESPONSE: String = "ERROR"

  def createNewGame(): Action[AnyContent] = Action {
    Ok(persistenceService.createGame())
  }

  def getGameBoard: Action[AnyContent] = Action { implicit request: Request[AnyContent] => {
      val id = returnRequestParamAsString(request, GAME_ID)
      val gameBoard = persistenceService.readGame(id)
      gameBoard match {
        case Some(board) => Ok(board)
        case _ => Ok(ERROR_RESPONSE)
      }
    }
  }

  def doMove(): Action[AnyContent] = Action { implicit request: Request[AnyContent] => {
      val id = returnRequestParamAsString(request, GAME_ID)
      val moveAsString = returnRequestParamAsString(request, MOVE_ID)
      persistenceService.updateGame(moveAsString, id) match {
        case Some(board) => Ok(board)
        case _ => Ok(ERROR_RESPONSE)
      }
    }
  }

  def deleteGame(): Action[AnyContent] = Action { implicit request: Request[AnyContent] => {
    val id = returnRequestParamAsString(request, GAME_ID)
    if (persistenceService.deleteGame(id)) Ok(SUCCESS_RESPONSE) else Ok(ERROR_RESPONSE)
  }}

  private def returnRequestParamAsString(request: Request[AnyContent], key: String): String = {
    request.body.asFormUrlEncoded.get(key).headOption.getOrElse(INVALID_RESPONSE)
  }

}
