package services.local

import ChessScala.controller.{Controller, IController}
import com.google.inject.Inject
import services.IGameService
import utils.CleanUpScheduler

import akka.actor.ActorSystem
import com.google.inject.Inject
import scala.collection.mutable
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt

class LocalPersistenceService @Inject()(actorSystem: ActorSystem)(implicit ec: ExecutionContext) extends IGameService {

  private val cleanUpIntervalTime = 1.minute
  private val garbageCollector: mutable.Map[String, Boolean] = mutable.Map.empty
  private var controllerMapping: Map[String, IController] = Map.empty

  def createGame(): String = {
    val gameID = java.util.UUID.randomUUID().toString
    val controller = new Controller()
    controllerMapping = controllerMapping + (gameID -> controller)
    garbageCollector.+=(gameID -> true)
    controller.computeInput("1")
    gameID
  }

  def readGame(id: String): Option[String] = {
    controllerMapping.get(id) match {
      case Some(controller) => Some(controller.returnBoardAsJson())
      case _ => None
    }
  }

  def updateGame(move: String, id: String): Option[String] = {
    controllerMapping.get(id) match {
      case Some(controller) =>
        controller.computeInput(move)
        garbageCollector(id) = true
        Some(controllerMapping(id).returnBoardAsJson())
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

  actorSystem.scheduler.scheduleAtFixedRate(initialDelay = cleanUpIntervalTime, interval = cleanUpIntervalTime) { () =>
    actorSystem.log.info("Executing clean up...")
    garbageCollector --= garbageCollector.filterNot(x => x._2).keys
    garbageCollector.foreach(x => garbageCollector(x._1) = false)
    controllerMapping = controllerMapping.filter(x => garbageCollector.contains(x._1))
  }
}