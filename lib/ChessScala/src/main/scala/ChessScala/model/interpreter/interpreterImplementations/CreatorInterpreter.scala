package ChessScala.model.interpreter.interpreterImplementations
import ChessScala.model.interpreter.Interpreter

class CreatorInterpreter extends Interpreter {

  override val descriptor: String = ""
  val move: String = "[a-hA-H][1-8][a-hA-H][1-8]"
  val moveWithSpace: String = "[a-hA-H][1-8] [a-hA-H][1-8]"
  val switchToGameState: String = "exit"
  val wrongInput: String = ".*"

  def doMove(input: String): (String, Boolean) = ("1", true)
  def doSwitchToGameState(input: String): (String, Boolean) = ("2", true)
  def doWrongInput(input: String): (String, Boolean) = ("wrong input!", false)

  override val actions: Map[String, String => (String, Boolean)] =
    Map((wrongInput, doWrongInput), (move, doMove), (moveWithSpace, doMove), (switchToGameState, doSwitchToGameState))
}
