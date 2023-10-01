package ChessScala.model.gameState.stateImplementation

import ChessScala.model.board.{Board, BoardBuilder}
import ChessScala.model.gameState.ProgrammState
import ChessScala.model.interpreter.Interpreter
import ChessScala.model.interpreter.interpreterImplementations.ConnectionInterpreter
import ChessScala.util.ConnectionHandler

class ConnectionState extends ProgrammState{

  override val interpreter: Interpreter = new ConnectionInterpreter()
  override val board: Board = (new BoardBuilder(8)).createChessBoard()

  override def handle(input: String): (ProgrammState, String) = {
    val interpreterResult = interpreter.processInputLine(input)
    if (!interpreterResult._2){
      return (this, interpreterResult._1)
    }
    if (input == "new") {
      val id = ConnectionHandler.createNewGameID().split("\n")
      val gameID: String = id(0)
      val playerID: String = id(1)
      ConnectionHandler.startPolling(gameID, playerID)
      println(gameID)
      return (new MultiplayerState(gameID, playerID, board), interpreterResult._1)
    }
    val id = ConnectionHandler.joinGame(input)
    ConnectionHandler.startPolling(input, id)
    (new MultiplayerState(input, id, board), interpreterResult._1)
  }

}
