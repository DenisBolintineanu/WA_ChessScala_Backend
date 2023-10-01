package ChessScala.model.gameState.stateImplementation

import ChessScala.model.board.Board
import ChessScala.model.gameState.ProgrammState
import ChessScala.model.interpreter.Interpreter
import ChessScala.model.interpreter.interpreterImplementations.GameInterpreter
import ChessScala.util.ConnectionHandler
import ChessScala.model.fileIO.fileIOJson.FileIO

class MultiplayerState(id: String, PlayerID: String, val board: Board) extends ProgrammState {
  override val interpreter: Interpreter = new GameInterpreter()
  val JsonBoardBuilder = new FileIO()

  override def handle(input: String): (ProgrammState, String) = {
    val interpreterResult = interpreter.processInputLine(input)
    if (!interpreterResult._2){
      return (this, interpreterResult._1)
    }
    val newState = doInput(input)
    if (newState.isInstanceOf[MateState])
      return (newState, "Checkmate")
    (newState, interpreterResult._1)
  }

  def doInput(input: String) : ProgrammState = {
    val result = ConnectionHandler.sendMoveRequest(id, PlayerID, input)
    val newBoard = JsonBoardBuilder.loadBoard(result)
    val stateString = JsonBoardBuilder.getState(result)
    if (stateString == "\"MateState\"") {
      ConnectionHandler.continuePolling = false
      return new MateState("Checkmate", newBoard)
    }
    ConnectionHandler.team = JsonBoardBuilder.loadTeam(result)
    new MultiplayerState(id, PlayerID, newBoard)
  }
}
