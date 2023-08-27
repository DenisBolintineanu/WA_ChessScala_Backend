package utils
import scala.collection.mutable

class GameSessionCollection {

  var sessionMap: mutable.Map[String, (GameSession, Boolean)] = mutable.Map.empty
  var playerMap: mutable.Map[String, GameSession] = mutable.Map.empty

  def create(): String = {
    val session: GameSession = GameSession()
    sessionMap.addOne(session.gameID, (session, true))
    playerMap ++= List((session.playerOneID, session), (session.playerTwoID, session))
    session.gameID
  }

  def delete(gameID: String): Unit = {
    sessionMap.get(gameID) match {
      case Some(session) => {
        sessionMap -= session._1.gameID
        playerMap -= session._1.playerOneID
        playerMap -= session._1.playerTwoID
      }
    }
  }

  def join(gameID: String): Option[String] = {
    sessionMap.get(gameID) match {
      case Some(session) => {
        if (!session._2) { return None }
        sessionMap(gameID) = (session._1, false)
        Some(session._1.playerTwoID)
      }
      case None => None
    }
  }

  def get(id: String): Option[GameSession] = {
    sessionMap.get(id) match {
      case Some(session) => Some(session._1)
      case None => playerMap.get(id)
    }
  }

  def filter(expression: String => Boolean): Unit = {
    sessionMap.keys.foreach(session => if (expression(session)) delete(session))
  }
}
