package controllers

import play.api.mvc._
import utils.CleanUpTask

import javax.inject._

@Singleton
class ClientController @Inject()(val controllerComponents: ControllerComponents, val cleanUpTask: CleanUpTask) extends BaseController {

  def createNewGame(): Action[AnyContent] = Action {
    val gameId = cleanUpTask.gameSetup
    Ok(gameId)
  }

  def doMove(id: String, moveAsString: String): Action[AnyContent] = Action {
    if (!cleanUpTask.controllerMapping.contains(id)) {
      Ok("ERROR")
    } else {
      val controller = cleanUpTask.controllerMapping(id)
      cleanUpTask.garbageCollector(id) = true
      controller.computeInput(moveAsString)
      Ok(controller.output)
    }
  }

}
