import com.redis._

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
    val r = new RedisClient("localhost", 6379)
    r.lpush(id, Map("timestamp" -> System.currentTimeMillis / 1000, "pos" -> Position(x,y)))
  }
}
