package ChessScala.util

trait Command {

  val inputString: String
  def doStep():Unit
  def undoStep():Unit
  def redoStep():Unit

}