package controllers

import ChessScala.controller.IController
import play.api.i18n.{I18nSupport, Lang}
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents, Request, Result}
import services.IPersistenceService
import utils.ChesspieceImageManager

import javax.inject.{Inject, Singleton}

@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents, val persistenceService: IPersistenceService) extends BaseController with I18nSupport {

  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def playChess2(id: String): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    gameExists(id).getOrElse()
    val move: String = request.body.asFormUrlEncoded.get("move").headOption.getOrElse("")
    persistenceService.updateGame(move, id) match {
      case Some(controller) => chessFromController(id, controller)
      case None => Ok(views.html.error())
    }
  }

  def newGame(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Redirect(routes.HomeController.joinGame(persistenceService.createGame()))
  }

  def joinGame(id: String): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    gameExists(id).getOrElse()
    persistenceService.readGame(id) match {
      case Some(controller) => chessFromController(id, controller)
      case None => Ok(views.html.error())

    }
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

  private def gameExists(id: String)(implicit request: Request[AnyContent]): Option[Result] = {
    persistenceService.readGame(id) match {
      case Some(_) => None
      case None => Some(Ok(views.html.gameNotFound(id)))
    }
  }

  private def chessFromController(id: String, controller: IController)(implicit request: Request[AnyContent]): Result = {
    Ok(views.html.chess(controller.output, id, controller.returnMoveList(), ChesspieceImageManager(controller.state.board)))
  }

}
