package services

import ChessScala.controller.{Controller, IController}

trait IPersistenceService {

  /**
   * Create a game
   * @return gameID
   */
  def createGame(): String
  def readGame(id: String): Option[IController]
  def updateGame(move: String, id: String): Option[IController]
  def deleteGame(id: String): Boolean
  def getGameIds: Map[String, Boolean]
}
