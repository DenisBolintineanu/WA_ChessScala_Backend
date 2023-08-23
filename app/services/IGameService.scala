package services

trait IGameService {

  /**
   * Create a game
   * @return gameID
   */
  def createGame(): String
  def readGame(id: String): Option[String]
  def updateGame(move: String, id: String): Option[String]
  def deleteGame(id: String): Boolean
}
