package ChessScala.model.interpreter.interpreterImplementations

import ChessScala.model.interpreter.Interpreter


class GameInterpreter extends Interpreter {

  override val descriptor: String = "Please enter your Move"

  val move:String = "[a-hA-H][1-8][a-hA-H][1-8]"
  val moveWithSpace: String = "[a-hA-H][1-8] [a-hA-H][1-8]"
  val undoMove: String = "undo"
  val redoMove: String = "redo"
  val wrongMove: String = ".*"
  val selectFigure: String = "[a-hA-H][1-8][a-hA-H][1-8](r|n|b|q)"


  def doUndoMove(input: String): (String, Boolean) =
    ("undo move",true)

  def doRedoMove(input: String): (String, Boolean) =
    ("redo move",true)

  def doMove(input: String): (String, Boolean) =
    (s"${input(0)}${input(1)} moved to ${input(2)}${input(3)}", true)

  def doSelectFigure(input: String): (String, Boolean) =
    (s"${input(0)}${input(1)} moved to ${input(2)}${input(3)}/figure selected", true)

  def doMoveWithSpace(input: String): (String, Boolean) =
    (f"${input(0)}${input(1)} moved to ${input(3)}${input(4)}", true)

  def doWrongMove(input:String): (String, Boolean) =
    ("Wrong move. Please try again.",false)

  override val actions: Map[String, String => (String, Boolean)] =
    Map((move,doMove), (moveWithSpace, doMoveWithSpace),(selectFigure, doSelectFigure))
}