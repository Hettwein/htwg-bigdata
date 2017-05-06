import com.redis._
import org.mongodb.scala.{Completed, MongoClient, MongoCollection, MongoDatabase, Observer}
import org.mongodb.scala.bson.collection.immutable.Document

/**
  * TODO
  *
  * Redis
  * https://github.com/debasishg/scala-redis
  *
  * Install Redis on Windows: https://github.com/MSOpenTech/redis/releases -> Redis-x64-3.2.100.msi
  * Start Server CMD: redis-cli, redis-cli shutdown, redis-server
  * Monitor Server CMD: redis-cli monitor
  */
class Database() {

  def updateAnt(id: String, x: Int, y: Int) {

    //val mongoClient: MongoClient = MongoClient("mongodb://localhost")

    val mongoClient: MongoClient = MongoClient()

    val doc: Document = Document("timestamp" -> System.currentTimeMillis / 1000, "id" -> id, "x" -> x, "y" -> y)


    val database: MongoDatabase = mongoClient.getDatabase("ants")

    val collection: MongoCollection[Document] = database.getCollection("ants")

    collection.insertOne(doc).subscribe(new Observer[Completed] {

      override def onNext(result: Completed): Unit = println("Inserted")

      override def onError(e: Throwable): Unit = println("Failed")

      override def onComplete(): Unit = println("Completed")
    })//.result?


    //val r = new RedisClient("localhost", 6379)
    //r.lpush(id, Map("timestamp" -> System.currentTimeMillis / 1000, "pos" -> Position(x,y)))
  }
}
