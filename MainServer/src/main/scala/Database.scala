import com.redis._

/**
  * TODO
  *
  * Redis
  * https://github.com/debasishg/scala-redis
  *
  * Install Redis on Windows: https://github.com/MSOpenTech/redis/releases -> Redis-x64-3.2.100.msi
  * Start Server CMD: redis-server
  * Monitor Server CMD: redis-cli monitor
  */
class Database {
  // Test: Set a key "key" and a value "some value"
  val r = new RedisClient("localhost", 6379)

  def updateAnt(id: String, x: Int, y: Int) {
    // TODO Server schreibt nach x writes nicht mehr.
    r.lpush(id, Map("timestamp" -> System.currentTimeMillis / 1000, "pos" -> Position(x,y)))
  }
}
