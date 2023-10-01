package ChessScala.model.gameState.stateImplementation

import ChessScala.model.board.Board
import ChessScala.model.figureStrategies.Team
import ChessScala.model.gameState.ProgrammState
import ChessScala.model.interpreter.Interpreter

class AIState(override val team: Team, override val board: Board, val selectedTeam: Team) extends GameState(team, board){
  override def handle(input: String): (ProgrammState, String) =
    val result = super.handle(input)
    if (result._2 == "Wrong move. Please try again.") return (this, "Wrong move! Please try again!")
    result._1 match {
      case state: GameState => (AIState(state.team, result._1.board, selectedTeam), result._2)
      case state: MateState => (state, state.result)
      case _ => (this, "Wrong move! Please try again!")
    }
}
