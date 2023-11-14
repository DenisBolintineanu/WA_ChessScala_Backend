package controllers

import ChessScala.controller.IController
import play.api.i18n.{I18nSupport, Lang}
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents, Request, Result}
import services.IPersistenceService
import utils.{ChesspieceImageManager, GameSession}
import play.api.libs.json.*

import scala.util.{Failure, Success, Try}
import utils.DefaultServerResponses.{ERROR_RESPONSE, INVALID_RESPONSE, SUCCESS_RESPONSE}

import javax.inject.{Inject, Singleton}

@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents, val persistenceService: IPersistenceService) extends BaseController with I18nSupport {

  private val MOVE_ID: String = "move"

  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
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


  def singleplayer():Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.singleplayer())
  }

  def localMultiplayer(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.local_multiplayer())
  }

  private def returnRequestParamAsString(request: Request[AnyContent], key: String): String = {
    request.body.asFormUrlEncoded.get(key).headOption.getOrElse(INVALID_RESPONSE)
  }
}


