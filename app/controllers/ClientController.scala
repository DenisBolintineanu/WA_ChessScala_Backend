package controllers

import ChessScala.controller.Controller
import play.api.Configuration
import play.api.mvc._
import services.IPersistenceService
import services.db.MongoDBService
import services.local.LocalPersistenceService
import utils.DefaultServerResponses.{ERROR_RESPONSE, INVALID_RESPONSE, SUCCESS_RESPONSE}

import javax.inject._

@Singleton
class ClientController @Inject()(val controllerComponents: ControllerComponents, val persistenceService: IPersistenceService) extends BaseController {

  private val MOVE_ID: String = "move"
  private val GAME_ID: String = "id"
  private val PLAYER_ID: String = "playerId"

  def createNewGame(): Action[AnyContent] = Action {
    Ok(persistenceService.createGame())
  }

  def getGameBoard: Action[AnyContent] = Action { implicit request: Request[AnyContent] => {
      val id = returnRequestParamAsString(request, GAME_ID)
      val gameBoard = persistenceService.readGame(id)
      gameBoard match {
        case Some(board) => Ok(board.returnBoardAsJson())
        case _ => Ok(ERROR_RESPONSE)
      }
    }
  }

  def doMove(): Action[AnyContent] = Action { implicit request: Request[AnyContent] => {
      val id = returnRequestParamAsString(request, GAME_ID)
      val moveAsString = returnRequestParamAsString(request, MOVE_ID)
      persistenceService.updateGame(moveAsString, id) match {
        case Some(board) => Ok(board.returnBoardAsJson())
        case _ => Ok(ERROR_RESPONSE)
      }
    }
  }

  def deleteGame(): Action[AnyContent] = Action { implicit request: Request[AnyContent] => {
    val id = returnRequestParamAsString(request, GAME_ID)
    if (persistenceService.deleteGame(id)) Ok(SUCCESS_RESPONSE) else Ok(ERROR_RESPONSE)
  }}

  def joinGame(): Action[AnyContent] = Action { implicit request: Request[AnyContent] => {
    val id = returnRequestParamAsString(request, GAME_ID)
    val playerID = persistenceService.joinGame(id)
    Ok(playerID)
  }}

  private def returnRequestParamAsString(request: Request[AnyContent], key: String): String = {
    request.body.asFormUrlEncoded.get(key).headOption.getOrElse(INVALID_RESPONSE)
  }

}
