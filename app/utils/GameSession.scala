package utils
import ChessScala.controller.IController
import ChessScala.controller.Controller

case class GameSession(){
  val controller: IController = new Controller()
  val gameID: String = java.util.UUID.randomUUID().toString
  val playerOneID: String = java.util.UUID.randomUUID().toString
  val playerTwoID: String = java.util.UUID.randomUUID().toString
  var PlayerOneMove: Option[String] = None
  var PlayerTwoMove: Option[String] = None
}
