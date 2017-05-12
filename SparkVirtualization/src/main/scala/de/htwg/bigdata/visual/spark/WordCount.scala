package de.htwg.bigdata.visual.spark

import com.mongodb.spark.MongoSpark

import com.mongodb.spark.config.ReadConfig
import com.typesafe.config.ConfigFactory
import org.bson.Document
import com.mongodb.spark.rdd.MongoRDD
import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.log4j._
import org.apache.spark.sql.SparkSession
import org.apache.spark.rdd.RDD
import de.htwg.bigdata.visual.resource.GridRequest
import scala.math.Ordering
import org.json4s.JsonDSL._
import org.json4s._
import org.json4s.jackson.JsonMethods._

case class GridRepresentation(step: Int, time: Long, fields: RDD[Document])

object WordCount {

  /** Count up how many of each word appears in a book as simply as possible. */

  /** Our main function where the action happens */
  def main(args: Array[String]) {
    transformGrid(new GridRequest("collection7", 80, 80, 1000))

  }

  def transformGrid2(gridRequest: GridRequest) = {

    val sparkSession = SparkSession.builder()
      .master("local")
      .appName("MongoSparkConnectorIntro")
      .config("spark.mongodb.input.uri", "mongodb://127.0.0.1/ants.collection7")
      .config("spark.mongodb.output.uri", "mongodb://127.0.0.1/ants.collection7")
      .getOrCreate()

    val rdd = MongoSpark.load(sparkSession.sparkContext)

    val columns = extractConfig(rdd)
    val antsPos = extractAntPos(rdd)

    rdd.toDF.createOrReplaceTempView("allEntries")

    val firstTimestamp = sparkSession.sql("SELECT MIN(timestamp) FROM allEntries").toJavaRDD.first().get(0)
    val lastTimestamp = sparkSession.sql("SELECT MAX(timestamp) FROM allEntries").toJavaRDD.first().get(0)
    var currentMillis = firstTimestamp.toString().toInt

    do {

      val currentAnts = sparkSession.sql("SELECT _id, id, timestamp, x, y FROM allEntries WHERE timestamp<" + currentMillis)

      //currentAnts.createOrReplaceTempView(viewName)

      //      val currentPos = antsPos.filter(doc => filterCurrentPos(doc, currentMillis))
      //      val currentAnts = currentPos.map(doc => (doc.getString("id").toInt, doc.getLong("timestamp").toInt))
      //        .map(item => item.swap).sortByKey(false, 1)
      //        .map(item => item.swap)
      //        .reduceByKey((a: Int, b: Int) => a)

      currentMillis += gridRequest.timestep
      //currentAnts2.foreach(doc => println(doc))
    } while (currentMillis < 4000)

  }

  def transformGrid(gridRequest: GridRequest) = {
    // Create a SparkContext using every core of the local machine
    val sc = new SparkContext("local[*]", "WordCount")
    val config = ConfigFactory.load()

    val readConfig = new ReadConfig(config.getString("mongodb.db"), gridRequest.collection, Some(config.getString("mongodb.uri")))
    val rdd = MongoSpark.load(sc, readConfig)

    val columns = extractConfig(rdd)
    val antsPos = extractAntPos(rdd)
    val antNumber = antsPos.map(doc => (doc.getString("id").toInt, doc.getLong("timestamp").toInt))
      .map(item => item.swap).sortByKey(false, 1)
      .map(item => item.swap)
      .reduceByKey((a: Int, b: Int) => a).count()

    val ratio = columns.first.getInteger("rows").toFloat / gridRequest.x.toFloat

    val firstTimestamp = antsPos.min()(TimeOrdering).getLong("timestamp")
    val lastTimestamp = antsPos.max()(TimeOrdering).getLong("timestamp")
    var currentMillis = 0

    var gridRepresentation = List[GridRepresentation]()
    var stepCount = 1
    do {
      val currentPos = antsPos.filter(doc => filterCurrentPos(doc, currentMillis))
      val currentAntsTuple = currentPos.map(doc => (doc.getString("id").toInt, doc.getLong("timestamp").toInt))
        .map(item => item.swap).sortByKey(false, 1)
        .map(item => item.swap)
        .reduceByKey((a: Int, b: Int) => a)

      val ants = currentAntsTuple.collect()

      val currentAnts = currentPos.filter(doc => {
        val id = doc.getString("id").toInt;
        val timestamp = doc.getLong("timestamp").toInt
        ants.contains((id, timestamp))
      })

      //calculate new x and y
      val currentAnts2 = currentAnts.map(doc =>
        {
          doc.append("newX", (doc.getInteger("x") * ratio).toInt)
          doc.append("newY", (doc.getInteger("y") * ratio).toInt)
          doc.append("posID", (doc.get("newX").toString() + "_" + doc.get("newY").toString()))
        }).map(doc => (doc.get("posID").toString().hashCode(), doc))

      val currentAnts3 = currentAnts2.reduceByKey((doc: Document, doc2: Document) => doc)
      val antCount = currentAnts2.countByKey()

      val currentsAnts4 = currentAnts3.map(doc => {
        doc._2.append("ants", (antCount.get(doc._1)) match {
          case Some(x: Long) => x // this extracts the value in a as an Int
          case _             => 0L
        })
        var conc=doc._2.getLong("ants").toFloat/ antNumber*100
        doc._2.append("concentration", conc)
      })

      //     val currentAnts3=currentAnts2.map(doc=>{
      //        doc._2.
      //     })

      currentsAnts4.foreach(doc => println(doc))
      antCount.foreach(doc => println(doc))
      println(antNumber)
      //build json

      val step = GridRepresentation(stepCount, currentMillis, currentAnts)
      gridRepresentation ::= step

      currentMillis += gridRequest.timestep
      //      println(antCount)
      stepCount = stepCount + 1
    } while (currentMillis <= 4000)

    gridRepresentation.foreach(g => println(g))

    // Flip (word, count) tuples to (count, word) and then sort by key (the counts)
    //val wordCountsSorted = wordCounts.map( x => (x._2, x._1) ).sortByKey()

  }

  //as

  private def extractConfig(rdd: MongoRDD[Document]) = rdd.filter(doc => doc.containsKey("rows"))
  private def extractAntPos(rdd: MongoRDD[Document]) = rdd.filter(doc => doc.containsKey("x"))
  private def filterCurrentPos(doc: Document, currentMillis: Long): Boolean = if (doc.getLong("timestamp") < currentMillis) true else false

  object TimeOrdering extends Ordering[Document] {
    def compare(doca: Document, docb: Document) = doca.getLong("timestamp").toInt compare docb.getLong("timestamp").toInt
  }

}

