import com.mongodb.casbah.Imports._

object DatabaseRead {

  var currentMillis:Long = 0;
  val mongoClient = MongoClient("localhost", 27017)
  val db = mongoClient("ants")

  def convertDbObjectToAnt(obj: MongoDBObject): AntPosition = {
    val id = obj.getAs[String]("id").get
    val x = obj.getAs[Int]("x").get
    val y = obj.getAs[Int]("y").get
    val timestamp = obj.getAs[Long]("timestamp").get
    AntPosition(id, x, y, timestamp)
  }

  def getFirstTimestamp():Long={

    val collection = db("ants")

    var ants = collection.find().limit(1)
    var timestamp:Long = 0
    for(antDBObject<-ants){
      var ant = convertDbObjectToAnt(antDBObject)
      timestamp = ant.timestamp
    }
    return timestamp
  }

  def readAnts():  List[AntPosition]={

    if(currentMillis ==0){
      currentMillis = getFirstTimestamp()
    }

    val collection = db("ants")

    var ants = collection.find("timestamp" $gte currentMillis $lt currentMillis+1000 )

    var antsSeq: List[AntPosition] = List()

    for(antDBObject<-ants){
      var ant = convertDbObjectToAnt(antDBObject)
      antsSeq :+= ant
    }

    currentMillis+=1000

    return antsSeq
  }
}
