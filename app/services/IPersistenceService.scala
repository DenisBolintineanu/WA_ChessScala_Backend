package services

import ChessScala.controller.IController
import utils.GameSessionCollection
import scala.collection.Map

trait IPersistenceService {

  /**
   * Create a game
   * @return gameID
   */
  val gameSessionCollection: GameSessionCollection
  def createGame(): String
  def readGame(id: String): Option[IController]
  def updateGame(move: String, id: String): Option[IController]
  def joinGame(id: String): String
  def deleteGame(id: String): Boolean
  def getGameIds: Map[String, Boolean]
}
