package services

import ChessScala.controller.IController

trait IPersistenceService {

  /**
   * Create a game
   * @return gameID
   */
  def createGame(): String
  def readGame(id: String): Option[IController]
  def updateGame(move: String, id: String, player: String): Option[IController]
  def joinGame(id: String): String
  def deleteGame(id: String): Boolean
  def getGameIds: Map[String, Boolean]
}
