package controllers

import play.api.i18n.I18nSupport
import play.api.libs.json.*
import play.api.mvc.{Action, *}
import services.IPersistenceService
import utils.DefaultServerResponses.INVALID_RESPONSE
import utils.GameSession

import javax.inject.{Inject, Singleton}

@Singleton
class MultiplayerController @Inject()(val controllerComponents: ControllerComponents, val persistenceService: IPersistenceService) extends BaseController with I18nSupport {

    def getMultiplayerPage: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
        Ok(views.html.online_multiplayer())
    }

    def start_new_game():Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
        val gameID = persistenceService.createGame()
        val session: GameSession = persistenceService.gameSessionCollection.get(gameID).get
        val playerID = session.playerOneID
        val JsonResponse = Json.obj("GameID" -> gameID, "PlayerID" -> playerID)
        Ok(JsonResponse)
    }

    def use_existing_game():Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
        val playerID = returnRequestParamAsString(request, "PlayerID")
        val jsonResponse: JsObject = persistenceService.gameSessionCollection.get(playerID) match {
            case Some(session) => {
                val color: String = if (playerID == session.playerOneID) "w" else "b"
                Json.obj("success" -> true, "FEN" -> session.FEN, "color" -> color)
            }
            case None => Json.obj("Success" -> false)
        }
        Ok(jsonResponse)
    }

    def joinGame(id: String): Action[AnyContent] = Action { implicit request =>
        persistenceService.gameSessionCollection.get(id) match
            case Some(session) =>
                val playerID = Cookie("PlayerID", session.playerTwoID, sameSite = Some(Cookie.SameSite.Strict), secure = true, httpOnly = false)
                val gameID = Cookie("GameID", id, sameSite = Some(Cookie.SameSite.Strict), secure = true, httpOnly = false)
                val color = Cookie("color", "b", sameSite = Some(Cookie.SameSite.Strict), secure = true, httpOnly = false)
                Redirect("/online_multiplayer").withCookies(playerID, gameID, color)
            case None => Ok(views.html.error())
    }

    private def setInternalMove(session: GameSession, move: String): Unit = {
        if (session.turn == session.playerOneID) {
            session.PlayerOneMove = Some(move)
        }
        else if (session.turn == session.playerTwoID) {
            session.PlayerTwoMove = Some(move)
        }
        session.switchTurn()
    }

    private def validateUciMove(session: GameSession, move: String, fen: String): JsObject = {
        persistenceService.updateGame(move, session.gameID) match
            case Some(_) =>
                setInternalMove(session, move)
                session.FEN = fen
                Json.obj("success" -> true)
            case None => Json.obj("success" -> false)
    }
    def doMove(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
        val playerID = returnRequestParamAsString(request, "PlayerID")
        val move = returnRequestParamAsString(request, "UCI")
        val fen = returnRequestParamAsString(request, "FEN")
        persistenceService.gameSessionCollection.get(playerID) match {
            case Some(session) =>
                if (session.isPlayerTurn(playerID)){
                    val jsonResponse: JsObject = validateUciMove(session, move, fen)
                    Ok(jsonResponse)
                }
                else Ok(Json.obj("success" -> false))
            case None => Ok(Json.obj("success" -> false))
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
                                "success" -> true,
                                "UCI" -> value
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
                                "success" -> true,
                                "UCI" -> value
                            )
                            Ok(jsonResponse)
                        }
                        case None => NoContent
                }
                else {
                    Ok(Json.obj("success" -> false))
                }
            case None => Ok(Json.obj("success" -> false))
        }
    }

    private def returnRequestParamAsString(request: Request[AnyContent], key: String): String = {
        request.body.asFormUrlEncoded.get(key).headOption.getOrElse(INVALID_RESPONSE)
    }
}


