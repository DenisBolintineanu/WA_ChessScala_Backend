package controllers

import ChessScala.controller.IController
import play.api.i18n.{I18nSupport, Lang}
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents, Request, Result}
import services.IPersistenceService
import utils.ChesspieceImageManager

import javax.inject.{Inject, Singleton}

@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents, val persistenceService: IPersistenceService) extends BaseController with I18nSupport {

  private val MOVE_ID: String = "move"

  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def doMove(id: String): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val move: String = request.body.asFormUrlEncoded.get(MOVE_ID).headOption.getOrElse("")
    persistenceService.updateGame(move, id) match {
      case Some(controller) => chessFromController(id, controller)
      case None => Ok(views.html.error())
    }
  }

  def newGame(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val gameId: String = persistenceService.createGame()
    val playerID: String = persistenceService.gameSessionCollection.get(gameId).get.playerOneID
    Redirect(routes.HomeController.updateGame(playerID))
  }

  def updateGame(id: String): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    persistenceService.readGame(id) match {
      case Some(controller) => chessFromController(id, controller)
      case None => Ok(views.html.error())
    }
  }

  def joinGame(id: String): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val playerID: String = persistenceService.joinGame(id)
    Redirect(routes.HomeController.updateGame(playerID))
  }

  def reloadCurrentPageWithLang(lang: String): Action[AnyContent] = Action { implicit request =>
    Redirect(request.headers.get(REFERER).getOrElse(routes.HomeController.index().url)).withLang(Lang(lang))
  }

  def adminPage(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.admin(Map.from(persistenceService.getGameIds)))
  }

  def rules(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.rules())
  }

  def about(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.about())
  }

  private def chessFromController(id: String, controller: IController)(implicit request: Request[AnyContent]): Result = {
    val gameID = persistenceService.gameSessionCollection.get(id).get.gameID
    Ok(views.html.chess(controller.output, id, gameID, controller.returnMoveList(), ChesspieceImageManager(controller.state.board)))
  }

  def singleplayer():Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.singleplayer())
  }
}


