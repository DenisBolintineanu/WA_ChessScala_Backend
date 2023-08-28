package services.local

import ChessScala.controller.{Controller, IController}
import ChessScala.model.figureStrategies.White
import ChessScala.model.gameState.stateImplementation.GameState
import com.google.inject.Inject
import services.IPersistenceService
import akka.actor.ActorSystem
import utils.{GameSession, GameSessionCollection}

import javax.inject.Singleton
import scala.collection.{mutable, Map}
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt

@Singleton
class LocalPersistenceService @Inject()(actorSystem: ActorSystem)(implicit ec: ExecutionContext) extends IPersistenceService {

  private val cleanUpIntervalTime = 10.minute
  private val garbageCollector: mutable.Map[String, Boolean] = mutable.Map[String, Boolean]()
  val gameSessionCollection: GameSessionCollection = new GameSessionCollection

  def createGame(): String = {
    val id: String = gameSessionCollection.create()
    garbageCollector(id) = true
    gameSessionCollection.get(id).get.controller.computeInput("1")
    id
  }

  def readGame(id: String): Option[IController] = {
    gameSessionCollection.get(id) match {
      case Some(value) => Some(value.controller)
      case None => None
    }
  }

  def updateGame(move: String, id: String): Option[IController] = {
    gameSessionCollection.get(id) match {
      case Some(session) =>
        val team = session.controller.state.asInstanceOf[GameState].team == White
        if (id == session.playerOneID && team || id == session.playerTwoID && !team)
          session.controller.computeInput(move)
        garbageCollector(id) = true
        Some(session.controller)
      case _ => None
    }
  }

  def deleteGame(id: String): Boolean = {
    gameSessionCollection.get(id) match {
      case Some(_) =>
        garbageCollector.remove(id)
        gameSessionCollection.delete(id)
        true
      case None => false
    }
  }

  override def getGameIds: Map[String, Boolean] = Map.from(garbageCollector)


  override def joinGame(id: String): String = {
    gameSessionCollection.join(id) match {
      case Some(value) => value
      case _ => ""
    }
  }


  actorSystem.scheduler.scheduleAtFixedRate(initialDelay = cleanUpIntervalTime, interval = cleanUpIntervalTime) { () =>
    actorSystem.log.info("Executing clean up...")
    garbageCollector --= garbageCollector.filterNot(x => x._2).keys
    garbageCollector.foreach(x => garbageCollector(x._1) = false)
    gameSessionCollection.filter(x => !garbageCollector.contains(x))
  }
}