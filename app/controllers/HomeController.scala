package controllers

import play.api.i18n.{I18nSupport, Lang}
import play.api.mvc._
import utils.{ChesspieceImageManager, CleanUpTask}

import javax.inject._

@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents, val cleanUpTask: CleanUpTask) extends BaseController with I18nSupport {

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
      val chesspieceImageManager: ChesspieceImageManager = new ChesspieceImageManager(controller.state.board)
      Ok(views.html.chess(controllerAsText, id, controller.returnMoveList(), chesspieceImageManager))
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
      val chesspieceImageManager: ChesspieceImageManager = new ChesspieceImageManager(controller.state.board)
      Ok(views.html.chess(controllerAsText, id, controller.returnMoveList(), chesspieceImageManager))
    }
  }

  def reloadCurrentPageWithLang(lang: String) = Action { implicit request =>
    Redirect(request.headers.get(REFERER).getOrElse(routes.HomeController.index().url)).withLang(Lang(lang))
  }

  def adminPage(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.admin(Map.from(cleanUpTask.garbageCollector)))
  }

  def rules(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.rules())
  }

  def about(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.about())
  }

}
