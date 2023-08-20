package controllers

import ChessScala.controller.Controller
import play.api.mvc._
import utils.CleanUpTask

import javax.inject._

@Singleton
class ClientController @Inject()(val controllerComponents: ControllerComponents, val cleanUpTask: CleanUpTask) extends BaseController {

  private val MOVE_ID: String = "move"
  private val GAME_ID: String = "id"
  private val INVALID_RESPONSE: String = "invalid"
  private val ERROR_RESPONSE: String = "ERROR"


  def createNewGame(): Action[AnyContent] = Action {
    val gameId = cleanUpTask.gameSetup
    Ok(gameId)
  }

  def doMove(): Action[AnyContent] = Action { implicit request: Request[AnyContent] => {
      val id = returnRequestParamAsString(request, GAME_ID)
      if (!cleanUpTask.controllerMapping.contains(id)) {
        Ok(ERROR_RESPONSE)
      } else {
        val moveAsString = returnRequestParamAsString(request, MOVE_ID)
        val controller = cleanUpTask.controllerMapping(id)
        cleanUpTask.garbageCollector(id) = true
        controller.computeInput(moveAsString)
        Ok(controller.returnBoardAsJson())
      }
    }
  }

  private def returnRequestParamAsString(request: Request[AnyContent], key: String): String = {
    request.body.asFormUrlEncoded.get(key).headOption.getOrElse(INVALID_RESPONSE)
  }

}
