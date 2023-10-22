package ChessScala.model.interpreter.interpreterImplementations
import ChessScala.model.interpreter.Interpreter

class ConnectionInterpreter extends Interpreter {
  override val descriptor: String = "Please enter an existing GameID oder type \"new\" for a new game"

  val existingGame : String = "([\\w|\\d]+-){4}[\\w|\\d]+"
  val newGame : String = "new"
  val wrongInput : String = ".*"

  def doNewGame(input: String): (String, Boolean) = ("", true)
  def doWrongInput(input: String): (String, Boolean) = ("Wrong input. Please try again.", false)


  override val actions: Map[String, String => (String, Boolean)] =
    Map((wrongInput, doWrongInput),(existingGame, doNewGame),(newGame,doNewGame))
}
