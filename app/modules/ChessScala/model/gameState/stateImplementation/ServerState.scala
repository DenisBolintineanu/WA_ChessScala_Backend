package ChessScala.model.gameState.stateImplementation

import ChessScala.model.gameState.ProgrammState
import ChessScala.model.figureStrategies.Team
import ChessScala.model.board.Board
import ChessScala.model.interpreter.Interpreter

class ServerState(gameState: GameState) extends GameState(gameState.team, gameState.board) {
    override def handle(input: String): (ProgrammState, String) = {
        val newBoard = gameState.handle(input)
        if (newBoard._1 == gameState) return (this, "Wrong move. Please try again.")
        newBoard._1 match
            case _: MateState => newBoard
            case newState: GameState => (new ServerState(newState), newBoard._2)
            case _ => (this, "Wrong move. Please try again.")
    }
}