package ChessScala.model.factory.factoryImplementation

import ChessScala.model.factory.Factory
import ChessScala.model.figureStrategies.*
import ChessScala.model.figureStrategies.strategyImplementations.{Bishop, Knight, Queen, Rook}

class FigureFactory(team : Team) extends Factory {

  def createFigure(figure : String) : Figure =
    figure.toLowerCase() match {
      case "bishop" | "b" => new Bishop(team)
      case "rook" | "r" => new Rook(team)
      case "knight" | "n" => new Knight(team)
      case "queen" | "q" => new Queen(team)
    }
}
