package controllers

import ChessScala.controller.Controller
import play.api.Configuration
import play.api.mvc._
import services.MongoDBService
import utils.CleanUpTask

import javax.inject._

@Singleton
class ClientController @Inject()(val controllerComponents: ControllerComponents, val cleanUpTask: CleanUpTask, config: Configuration) extends BaseController {

  private val MOVE_ID: String = "move"
  private val GAME_ID: String = "id"
  private val INVALID_RESPONSE: String = "invalid"
  private val ERROR_RESPONSE: String = "ERROR"
  private val database = new MongoDBService(config)


  def createNewGame(): Action[AnyContent] = Action {
    val id = cleanUpTask.gameSetup
    database.writeGame(id, "")
    Ok(id)
  }

  def getGameBoard: Action[AnyContent] = Action { implicit request: Request[AnyContent] => {
      val id = returnRequestParamAsString(request, GAME_ID)
      if (!cleanUpTask.controllerMapping.contains(id)) {
        Ok(ERROR_RESPONSE)
      }
      val controller = cleanUpTask.controllerMapping(id)
      Ok(controller.returnBoardAsJson())
    }
  }

  def doMove(): Action[AnyContent] = Action { implicit request: Request[AnyContent] => {
      val id = returnRequestParamAsString(request, GAME_ID)
      val moveAsString = returnRequestParamAsString(request, MOVE_ID)
      val gameAsStrings = database.readGame(id).get.split(" ")
      val controller = new Controller()
      controller.computeInput("3")
      gameAsStrings.reverse.foreach(k => controller.computeInput(k))
      controller.computeInput("exit")
      controller.computeInput(moveAsString)
      val movelist = controller.returnMoveList()
      database.writeGame(id, controller.returnMoveList().mkString(" "))
      Ok(controller.returnBoardAsJson())
    }
  }

  private def returnRequestParamAsString(request: Request[AnyContent], key: String): String = {
    request.body.asFormUrlEncoded.get(key).headOption.getOrElse(INVALID_RESPONSE)
  }



}
