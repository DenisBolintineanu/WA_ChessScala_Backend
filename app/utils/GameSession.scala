package utils
import ChessScala.controller.IController
import ChessScala.controller.Controller
import akka.actor.ActorRef
import play.api.libs.json.Json

case class GameSession(){
  val controller: IController = new Controller()
  val gameID: String = java.util.UUID.randomUUID().toString
  val playerOneID: String = java.util.UUID.randomUUID().toString
  val playerTwoID: String = java.util.UUID.randomUUID().toString
  var PlayerOneMove: Option[String] = None
  var PlayerTwoMove: Option[String] = None
  var playerOneWebSocket: Option[ActorRef] = None
  var playerTwoWebSocket: Option[ActorRef] = None
  var FEN: String = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
  var turn: String = playerOneID
  def switchTurn(): Unit = turn = if (turn == playerOneID) playerTwoID else playerOneID
  def isPlayerTurn(playerID: String): Boolean = playerID == turn
  
  def setPlayerOneMove(move: String): Unit =
    playerTwoWebSocket match
      case Some(out) => 
        out ! Json.obj("success" -> true, "UCI" -> move)
        playerTwoWebSocket = None
      case None => PlayerOneMove = Some(move)

    def setPlayerTwoMove(move: String): Unit =
      playerOneWebSocket match
        case Some(out) =>
          out ! Json.obj("success" -> true, "UCI" -> move)
          playerOneWebSocket = None
        case None => PlayerTwoMove = Some(move)
}
