package controllers

import ChessScala.controller.IController
import play.api.mvc.*
import services.IPersistenceService
import utils.DefaultServerResponses.{ERROR_RESPONSE, INVALID_RESPONSE, SUCCESS_RESPONSE}
import play.api.libs.json._

import javax.inject.*

@Singleton
class ClientController @Inject()(val controllerComponents: ControllerComponents, val persistenceService: IPersistenceService) extends BaseController {

  private val MOVE_ID: String = "move"
  private val GAME_ID: String = "id"
  private val PLAYER_ID: String = "PlayerID"

  def createNewGame(): Action[AnyContent] = Action {
    val id = persistenceService.createGame()
    Ok(id + "\n" + persistenceService.gameSessionCollection.get(id).get.playerOneID)
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

  def doMove(): Action[AnyContent] = Action {
    val json: JsValue = Json.parse("""{"result": "it works"}""")
    Ok(json)
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
