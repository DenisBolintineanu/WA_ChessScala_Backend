package services

trait IPersistenceService {

  /**
   * Create a game
   * @return gameID
   */
  def createGame(): String
  def readGame(id: String, asJson: Boolean = true): Option[String]
  def updateGame(move: String, id: String, asJson: Boolean = true): Option[String]
  def deleteGame(id: String): Boolean
}
