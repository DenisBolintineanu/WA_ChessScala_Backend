package ChessScala.model.gameState.stateImplementation

import ChessScala.model.board.{Board, BoardBuilder}
import ChessScala.model.figureStrategies.{Black, Team, White}
import ChessScala.model.gameState.ProgrammState
import ChessScala.model.interpreter.Interpreter
import ChessScala.model.interpreter.interpreterImplementations.CreatorInterpreter
import ChessScala.model.moveChain.{Move, MoveDecoder}

class BoardCreatorState(val team: Team, val board: Board) extends ProgrammState{
  override val interpreter: Interpreter = new CreatorInterpreter

  override def handle(input: String): (ProgrammState, String) = {
    val resultString = interpreter.processInputLine(input)
    if (!resultString._2) return (this, "")
    if (resultString._1 == "2") return (new GameState(team, board),"")
    val move: Move = MoveDecoder.decode(input)
    if (!board.is_occupied(move.start)) return (this,"")
    val newBoard: Board = board.insert(move.target, board.get(move.start)).delete(move.start)
    val nextTeam: Team = if (team == White) Black else White
    (new BoardCreatorState(nextTeam, newBoard), "")
  }
}
