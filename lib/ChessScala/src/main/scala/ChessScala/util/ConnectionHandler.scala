package ChessScala.util
import ChessScala.model.board.{Board, BoardBuilder}
import ChessScala.model.figureStrategies.{Team, White}
import ChessScala.model.gameState.ProgrammState
import ChessScala.model.gameState.stateImplementation.{MateState, MenuState, MultiplayerState}
import sttp.client3.*
import ChessScala.model.fileIO.fileIOJson.FileIO

object ConnectionHandler extends Observable {
  val backend: SttpBackend[Identity, Any] = HttpURLConnectionBackend()
  val ip: String = "localhost"//167.235.66.0"
  val port: String = "9000"
  val pathForNewGame: String = "/clientRequest/createNewGameID"
  val pathForMoveRequest: String = "/clientRequest/move"
  val pathForPolling: String = "/clientRequest/gameboard"
  val pathForJoining: String = "/clientRequest/join"

  def createNewGameID(): String = {
    val requestString = "http://" + ip + ":" + port + pathForNewGame
    val request = basicRequest.get(uri"$requestString")
    request.send(backend).body.getOrElse("")
  }

  def joinGame(id: String): String = {
    val requestString = "http://" + ip + ":" + port + pathForJoining
    val request = basicRequest.post(uri"$requestString").body(Map("id" -> id))
    request.send(backend).body.getOrElse("")
  }

  def sendMoveRequest(id: String, PlayerID: String, move: String) : String = {
    val requestString = "http://" + ip + ":" + port + pathForMoveRequest
    val request = basicRequest.post(uri"$requestString").body(Map("id" -> id,"PlayerID" -> PlayerID, "move" -> move))
    request.send(backend).body.getOrElse("")
  }

  @volatile var continuePolling: Boolean = false
  @volatile var state: ProgrammState = new MenuState
  @volatile var team: Team = White

  def startPolling(id: String, PlayerID: String): Unit = {
    new Thread(() => {
      val requestString: String = "http://" + ip + ":" + port + pathForPolling
      val JsonBoardBuilder = new FileIO()
      continuePolling = true
      while (continuePolling) {
        Thread.sleep(500)
        val request = basicRequest.post(uri"$requestString").body(Map("id" -> id))
        val response: String = request.send(backend).body.getOrElse("")
        val newBoard = JsonBoardBuilder.loadBoard(response)
        val stateString = JsonBoardBuilder.getState(response)
        if (stateString == "\"MateState\""){
          state = new MateState("Checkmate", newBoard)
          continuePolling = false
        }
        if (JsonBoardBuilder.loadTeam(response) != team){
          state = new MultiplayerState(id, PlayerID, newBoard)
          team = JsonBoardBuilder.loadTeam(response)
          notifyObservers()
        }
      }
    }).start()
  }
}
