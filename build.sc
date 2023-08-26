import org.mongodb.scala._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Updates._
import org.mongodb.scala.model.ReplaceOptions
import scala.concurrent.Await
import scala.concurrent.duration._

val mongoClient: MongoClient = MongoClient("mongodb://localhost:27017")
val database: MongoDatabase = mongoClient.getDatabase("chessDB")
val collection: MongoCollection[Document] = database.getCollection("games")

def writeGame(gameID: String, notation: String): Unit = {
  val doc: Document = Document("_id" -> gameID, "notation" -> notation)
  val replaceOptions = ReplaceOptions().upsert(true) // Option zum Einfügen, wenn nicht vorhanden
  Await.result(collection.replaceOne(equal("_id", gameID), doc, replaceOptions).toFuture(), 10.seconds)
}

def readGame(gameID: String): Option[String] = {
  val futureResult = collection.find(equal("_id", gameID)).first().toFuture()
  // Blockierender Aufruf für Demonstrationszwecke
  val result = Await.result(futureResult, 10.seconds)
  println(result)
  if (result.isEmpty) None
  else Some(result("notation").asString().getValue)
}

writeGame("game1", "e2e3 e7e5")
val x = readGame("game1")
println(x)