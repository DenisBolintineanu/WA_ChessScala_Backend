package ChessScala.model.fileIO.fileIOJson

import ChessScala.model.board.{Board, BoardBuilder, Coordinate}
import ChessScala.model.factory.factoryImplementation.IdFactory
import ChessScala.model.figureStrategies.{Black, Figure, Team, White}
import ChessScala.model.fileIO.FileIOInterface
import ChessScala.model.gameState.*
import ChessScala.model.gameState.stateImplementation.{GameState, SelectState, MateState}
import com.google.inject.Guice
import com.google.inject.name.Names
import net.codingwell.scalaguice.InjectorExtensions.*

import scala.util.{Failure, Success, Try}
import play.api.libs.json.*

import scala.io.Source

class FileIO extends FileIOInterface {

  override def save(state: ProgrammState): Unit =
    import java.io._
    val pw = new PrintWriter(new File("Chess.json"))
    pw.write(Json.prettyPrint(gameToJson(state)))
    pw.close

  def coordinateToJson(coordinate: Coordinate, board: Board): JsObject =
    Json.obj(
      "coordinate" -> Json.obj(
        "x" -> JsNumber(coordinate.x),
        "y" -> JsNumber(coordinate.y),
        "figure" -> JsString(figureToString(board.get(coordinate)))
      ))

  def figureToString(figure: Option[Figure]): String =
    if (figure.isDefined) figure.get.id.toString
    else "None"


  def gameToJson(state: ProgrammState, increment: Int = 0): JsObject =
    Json.obj(
    "game" -> Json.obj(
      "state" -> JsString(state match {
        case gameState: GameState => "GameState"
        case mateState: MateState => "MateState"
      }),
      "team" -> (state match {
        case gameState: GameState => JsString(teamToString(gameState))
        case _ => JsString("")
      }),
      "board" -> Json.toJson(for {field <- state.board.map if field._2.isDefined} yield coordinateToJson(field._1, state.board))
    ))

  def teamToString(state: GameState): String = if (state.team == White) "White" else "Black"


  override def load(path: String): ProgrammState = {

    val file: String = Source.fromFile(path + ".json").getLines().mkString
    val team = loadTeam(file)
    val board = loadBoard(file)
    new GameState(team, board)
  }

  def getState(JsonString: String): String = {
    val json: JsValue = Json.parse(JsonString)
    (json \ "game" \ "state").get.toString
  }

  def loadTeam(JsonString: String): Team = {
    val json: JsValue = Json.parse(JsonString)
    val teamString: String = (json \ "game" \ "team").get.toString
    val team: Team = if (teamString == "\"White\"") White else Black
    team
  }

  def loadBoard(JsonString: String): Board = {
    val builder = new BoardBuilder(8)
    var board: Board = builder.createEmptyBoard()
    val json: JsValue = Json.parse(JsonString)
    val fields = (json \ "game" \ "board" \\ "coordinate")
    for (field <- fields) {
      val x: Int = (field \ "x").get.toString.toInt
      val y: Int = (field \ "y").get.toString.toInt

      val coordinate: Coordinate = Coordinate(x, y)
      val id: String = (field \ "figure").get.as[String]

      val factory = new IdFactory
      val tryFigure: Try[Figure] = Try {
        factory.createFigure(id)
      }
      val figure: Option[Figure] = tryFigure match {
        case Success(value) => Some(value)
        case Failure(_) => None
      }
      board = board.insert(coordinate, figure)
    }
    board
  }
}
