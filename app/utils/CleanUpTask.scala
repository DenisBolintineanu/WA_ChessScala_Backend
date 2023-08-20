package utils

import ChessScala.controller.{Controller, IController}
import akka.actor.ActorSystem
import com.google.inject.Inject

import scala.collection.mutable
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt

class CleanUpTask @Inject()(actorSystem: ActorSystem)(implicit executionContext: ExecutionContext) {

  val garbageCollector: mutable.Map[String, Boolean] = mutable.Map.empty
  var controllerMapping: Map[String, IController] = Map.empty

  private val cleanUpIntervalTime = 7.minute

  def gameSetup: String = {
    val gameID = java.util.UUID.randomUUID().toString
    val controller = new Controller()
    controllerMapping = controllerMapping + (gameID -> controller)
    garbageCollector.+=(gameID -> true)
    controller.computeInput("1")
    gameID
  }

  actorSystem.scheduler.scheduleAtFixedRate(initialDelay = cleanUpIntervalTime, interval = cleanUpIntervalTime) { () =>
    actorSystem.log.info("Executing clean up...")
    garbageCollector --= garbageCollector.filterNot(x => x._2).keys
    garbageCollector.foreach(x => garbageCollector(x._1) = false)
    controllerMapping = controllerMapping.filter(x => garbageCollector.contains(x._1))
  }
}
