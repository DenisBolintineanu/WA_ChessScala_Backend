package ChessScala.model.interpreter.interpreterImplementations

import ChessScala.model.figureStrategies.Team
import ChessScala.model.interpreter.Interpreter

class SelectInterpreter extends Interpreter {

  override val descriptor: String = "Select a figure"

  def selectFigureShort(figure: String):(String, Boolean) =
    figure match
      case "q" => ("Queen selected", true)
      case "n" => ("Knight selected", true)
      case "r" => ("Rook selected", true)
      case "b" => ("Bishop selected", true)
      case _ => selectFigure(figure)

  def selectFigure(figure : String): (String, Boolean) =

    figure.toLowerCase() match
      case "queen" => ("Queen selected", true)
      case "knight" => ("Knight selected", true)
      case "rook" => ("Rook selected", true)
      case "bishop" => ("Bishop selected", true)
      case _ => ("Non eligible figure", false)


  val actions: Map[String, String => (String, Boolean)] = Map((".*", selectFigureShort))


}
