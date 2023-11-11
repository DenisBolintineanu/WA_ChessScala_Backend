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

  def doMove(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val playerID = returnRequestParamAsString(request, "PlayerID")
    val move = returnRequestParamAsString(request, "move")
    persistenceService.gameSessionCollection.get(playerID) match {
      case Some(session) => {
        if (playerID == session.playerOneID) {
          persistenceService.updateGame(move, session.gameID) match {
            case Some(value) =>
              session.PlayerOneMove = Some(move)
              Ok(Json.parse("""{"result": "success"}"""))
            case None => Ok(Json.parse("""{"result": "error"}"""))
          }
        }
        else if (playerID == session.playerTwoID) {
          persistenceService.updateGame(move, session.gameID) match {
            case Some(value) =>
              session.PlayerTwoMove = Some(move)
              Ok(Json.parse("""{"result": "success"}"""))
            case None => Ok(Json.parse("""{"result": "error"}"""))
          }
        }
        else {
          Ok(Json.parse("""{"result": "error"}"""))
        }
      }
      case None => Ok(Json.parse("""{"result": "error"}"""))
    }
  }

  def getMove: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val playerID = returnRequestParamAsString(request, "PlayerID")
    persistenceService.gameSessionCollection.get(playerID) match {
      case Some(session) =>
        if (playerID == session.playerOneID) {
          session.PlayerTwoMove match
            case Some(value) => {
              session.PlayerTwoMove = None
              val jsonResponse = Json.obj(
                "result" -> "success",
                "move" -> value
              )
              Ok(jsonResponse)
            }
            case None => NoContent
        }
        else if (playerID == session.playerTwoID) {
          session.PlayerOneMove match
            case Some(value) => {
              session.PlayerOneMove = None
              val jsonResponse = Json.obj(
                "result" -> "success",
                "move" -> value
              )
              Ok(jsonResponse)
            }
            case None => NoContent
        }
        else {
          Ok(Json.parse("""{"result": "error"}"""))
        }
      case None => Ok(Json.parse("""{"result": "error"}"""))
    }
  }

  def newGame(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val gameId: String = persistenceService.createGame()
    val playerID: String = persistenceService.gameSessionCollection.get(gameId).get.playerOneID
    persistenceService.readGame(playerID) match {
      case Some(controller) => chessFromController(playerID, controller)
      case None => Ok(views.html.error())
    }
  }

  def joinGame(gameId: String): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Try{ persistenceService.gameSessionCollection.get(gameId).get.playerTwoID} match {
      case Success(playerID) => {
        persistenceService.readGame(playerID) match {
          case Some(controller) => chessFromController(playerID, controller)
          case None => Ok(views.html.error())
        }
      }
      case Failure(_) => Ok(views.html.error())
    }
  }



  def updateGame(id: String): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
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

  private def chessFromController(id: String, controller: IController)(implicit request: Request[AnyContent]): Result = {
    val gameID = persistenceService.gameSessionCollection.get(id).get.gameID
    Ok(views.html.online_multiplayer(id, gameID))
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


