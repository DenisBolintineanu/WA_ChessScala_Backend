package ChessScala.controller
import ChessScala.model.gameState.ProgrammState
import ChessScala.model.gameState.stateImplementation.{BoardCreatorState, GameState}
import ChessScala.util.Command
import ChessScala.util.UndoManager

class SetCommand(input: String, controller: Controller) extends Command {

  val state: ProgrammState = controller.state match {
      case state1: BoardCreatorState => new GameState(state1.team, state1.board)
      case _ => controller.state
    }
  override val inputString: String = input

  override def doStep(): Unit = {
    val result = controller.state.handle(input)
    if (result._1 == controller.state) {
      controller.undoManager.deleteStep()
      controller.output = result._2
      return
    }
    controller.state = result._1
    controller.output = result._2
  }
  override def undoStep(): Unit =
    controller.state = state


  override def redoStep(): Unit = {
    val result = controller.state.handle(input)
    controller.state = result._1
    controller.output = result._2
  }
}