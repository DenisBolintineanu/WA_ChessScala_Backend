/*package services.db

import com.google.inject.Inject
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.ReplaceOptions
import org.mongodb.scala.{Document, MongoClient, MongoCollection, MongoDatabase}
import play.api.Configuration

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

class MongoDBService @Inject()() (config: Configuration) {
  private val mongoUri: String = config.get[String]("mongodb.uri")
  private val dbName: String = config.get[String]("mongodb.database")
  private val collectionName: String = config.get[String]("mongodb.collection")

  val mongoClient: MongoClient = MongoClient(mongoUri)
  val database: MongoDatabase = mongoClient.getDatabase(dbName)
  val collection: MongoCollection[Document] = database.getCollection(collectionName)

  def writeGame(gameID: String, notation: String): Unit = {
    val doc: Document = Document("_id" -> gameID, "notation" -> notation)
    val replaceOptions = ReplaceOptions().upsert(true) // Option zum Einfügen, wenn nicht vorhanden
    Await.result(collection.replaceOne(equal("_id", gameID), doc, replaceOptions).toFuture(), 10.seconds)
  }

  def readGame(gameID: String): Option[String] = {
    val futureResult = collection.find(equal("_id", gameID)).first().toFuture()
    val result = Await.result(futureResult, 10.seconds)
    println(result)
    if (result.isEmpty) None
    else Some(result("notation").asString().getValue)
  }
}
*/