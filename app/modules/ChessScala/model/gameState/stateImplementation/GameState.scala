package ChessScala.model.gameState.stateImplementation

import ChessScala.model.board.Board
import ChessScala.model.figureStrategies.Team
import ChessScala.model.fileIO.fileIOJson.FileIO
import ChessScala.model.gameState.ProgrammState
import ChessScala.model.interpreter.Interpreter
import ChessScala.model.interpreter.interpreterImplementations.GameInterpreter
import ChessScala.model.moveChain.ChainImplementations.MoveHandler
import ChessScala.model.moveChain.{GameChain, MoveDecoder}

import scala.util.{Failure, Success, Try}
import scala.annotation.retains


class GameState(val team: Team, override val board: Board) extends ProgrammState {


  override val interpreter: Interpreter = new GameInterpreter


  override def handle(input: String): (ProgrammState, String) = {
    if (input.length() < 4) return (this, "Wrong move. Please try again.")
    if (input == "save") {
      fileIO.save(this)
      return (this, "Game saved")
    } else if (input == "load") {
      return (fileIO.load("Chess"), "Game loaded")
    }
    val result = interpreter.processInputLine(input)

    if (result._1.contains("figure selected")) handleSelectMove(input, result)
    else handleNormalMove(input, result)
  }

    def handleNormalMove(input: String, result: (String, Boolean)): (ProgrammState, String) = {
      val chain: GameChain = new MoveHandler(MoveDecoder.decode(input))
      val success = Try {
        chain.handle(this).get
      }
      success match {
        case Failure(_) => return (this, "Wrong move. Please try again.")
        case Success(value) => {
          value match
            case _: MateState => return (value, value.asInstanceOf[MateState].result)
            case _ => return (value, result._1)
        }
      }
    }

    def handleSelectMove(input: String, result: (String, Boolean)): (ProgrammState, String) = {
      val newState: ProgrammState = handleNormalMove(input, result)._1
      if (!newState.isInstanceOf[SelectState])
        return (this, "Wrong move. Please try again.")
      val selectedFigure: String = input.last.toString
      val newState2 = (newState.handle(selectedFigure)._1, result._1.split("/")(0))
      if (newState2._1.isInstanceOf[SelectState])
        return (this, "Wrong move. Please try again.")
      newState2
    }
}
