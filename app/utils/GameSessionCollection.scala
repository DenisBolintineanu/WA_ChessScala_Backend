package utils
import scala.collection.mutable

class GameSessionCollection {

  var sessionMap: mutable.Map[String, GameSession] = mutable.Map.empty
  var playerMap: mutable.Map[String, GameSession] = mutable.Map.empty

  def create(): String = {
    val session: GameSession = GameSession()
    sessionMap.addOne(session.gameID, session)
    sessionMap ++= List((session.playerOneID, session), (session.playerTwoID, session))
    session.gameID
  }

  def delete(gameID: String): Unit = {
    sessionMap.get(gameID) match {
      case Some(session) => {
        sessionMap -= session.gameID
        playerMap -= session.playerOneID
        playerMap -= session.playerTwoID
      }
    }
  }

  def get(id: String): Option[GameSession] = {
    sessionMap.get(id) match {
      case Some(session) => Some(session)
      case None => playerMap.get(id)
    }
  }

  def filter(expression: String => Boolean): Unit = {
    sessionMap.keys.foreach(session => if (expression(session)) delete(session))
  }
}
