package controllers

import akka.actor.*
import akka.stream.Materializer
import com.google.inject.Singleton
import play.api.libs.json.{JsValue, Json}
import play.api.libs.streams.ActorFlow
import play.api.mvc.*
import services.IPersistenceService
import utils.DefaultServerResponses.INVALID_RESPONSE
import utils.GameSession

import javax.inject.Inject

@Singleton
class MultiplayerWebSocketController @Inject()(val persistenceService: IPersistenceService, cc: ControllerComponents) (implicit system: ActorSystem, mat: Materializer) extends AbstractController(cc) {

    def socket: WebSocket = WebSocket.accept[String, String] { request =>
        ActorFlow.actorRef { out =>
            Props(new MultiplayerWebSocketActor(out))
        }
    }

    private def returnRequestParamAsString(request: Request[AnyContent], key: String): String = {
        request.body.asFormUrlEncoded.get(key).headOption.getOrElse(INVALID_RESPONSE)
    }
    private class MultiplayerWebSocketActor(out: ActorRef) extends Actor{
        def receive: Receive = {
            case msg: String =>
                val json: JsValue = Json.parse(msg)
                (json \ "type").as[String] match
                    case "register" => register(json)
                    case "move" => doMove(json)
                    case "timeout" =>
                    case _ => println("not supported")
        }

        private def register(msg: JsValue): Unit =
            persistenceService.gameSessionCollection.get((msg \ "PlayerID").as[String]) match
                case None =>
                case Some(session) =>
                    if ((msg \ "PlayerID").as[String] == session.playerOneID)
                        session.playerOneWebSocket = Some(out)
                    else session.playerTwoWebSocket = Some(out)


        private def doMove(msg: JsValue): Unit =
            persistenceService.gameSessionCollection.get((msg \ "PlayerID").as[String]) match
                case None =>
                case Some(session) =>
                    persistenceService.updateGame((msg \ "UCI").as[String], (msg \ "PlayerID").as[String]) match
                        case None =>
                        case Some(value) =>
                            session.FEN = (msg \ "FEN").as[String]
                            sendMove((msg \ "PlayerID").as[String] , (msg \ "UCI").as[String])


        private def sendMove(playerID: String, move: String): Unit =
            val session: GameSession = persistenceService.gameSessionCollection.get(playerID).get
            (if (playerID == session.playerOneID) session.playerTwoWebSocket else session.playerOneWebSocket) match
                case None =>
                case Some(opponent) =>
                    opponent ! Json.obj("UCI" -> move).toString
    }
}
