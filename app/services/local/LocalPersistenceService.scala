package services.local

import ChessScala.controller.{Controller, IController}
import ChessScala.model.figureStrategies.White
import ChessScala.model.gameState.stateImplementation.GameState
import com.google.inject.Inject
import services.IPersistenceService
import akka.actor.ActorSystem
import utils.GameSession

import javax.inject.Singleton
import scala.collection.mutable
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt

@Singleton
class LocalPersistenceService @Inject()(actorSystem: ActorSystem)(implicit ec: ExecutionContext) extends IPersistenceService {

  private val cleanUpIntervalTime = 10.minute
  private val garbageCollector: mutable.Map[String, Boolean] = mutable.Map.empty
  private var controllerMapping: Map[String, GameSession] = Map.empty

  def createGame(): String = {
    val gameID = java.util.UUID.randomUUID().toString
    val Player1_ID = java.util.UUID.randomUUID().toString
    val Player2_ID = java.util.UUID.randomUUID().toString
    val controller: IController = new Controller()
    val GameSession: GameSession = new GameSession(Player1_ID, Player2_ID, controller)
    controllerMapping = controllerMapping + (gameID -> GameSession)
    garbageCollector.+=(gameID -> true)
    controller.computeInput("1")
    gameID + "\n" + Player1_ID
  }

  def readGame(id: String): Option[IController] = {
    controllerMapping.get(id).map(_.controller)
  }

  def updateGame(move: String, id: String, player: String): Option[IController] = {
    controllerMapping.get(id) match {
      case Some(session) =>
        val team = session.controller.state.asInstanceOf[GameState].team == White
        if (player == session.PlayerOne && team || player == session.PlayerTwo && !team)
          session.controller.computeInput(move)
        garbageCollector(id) = true
        Some(controllerMapping(id).controller)
      case _ => None
    }
  }

  def deleteGame(id: String): Boolean = {
    controllerMapping.get(id) match {
      case Some(_) =>
        garbageCollector.remove(id)
        controllerMapping = controllerMapping.filter(x => garbageCollector.contains(x._1))
        true
      case None => false
    }
  }

  override def getGameIds: Map[String, Boolean] = Map.from(garbageCollector)


  override def joinGame(id: String): String = {
    controllerMapping.get(id) match {
      case Some(session) => session.PlayerTwo
      case _ => ""
    }
  }


  actorSystem.scheduler.scheduleAtFixedRate(initialDelay = cleanUpIntervalTime, interval = cleanUpIntervalTime) { () =>
    actorSystem.log.info("Executing clean up...")
    garbageCollector --= garbageCollector.filterNot(x => x._2).keys
    garbageCollector.foreach(x => garbageCollector(x._1) = false)
    controllerMapping = controllerMapping.filter(x => garbageCollector.contains(x._1))
  }
}