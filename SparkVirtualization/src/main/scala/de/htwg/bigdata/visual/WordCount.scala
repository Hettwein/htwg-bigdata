package de.htwg.bigdata.visual

import com.mongodb.spark.MongoSpark
import com.mongodb.spark.config.ReadConfig
import com.typesafe.config
import com.typesafe.config.ConfigFactory
import org.bson.Document
import com.mongodb.spark.rdd.MongoRDD
import org.apache.spark.rdd.RDD

object WordCount {
  import org.apache.spark._
  import org.apache.spark.SparkContext._
  import org.apache.log4j._
  import org.apache.spark.sql.SparkSession

  var currentMillis: Long = 0;
  var firstTimestamp: Long = 0;

  /** Count up how many of each word appears in a book as simply as possible. */

  /** Our main function where the action happens */
  def main(args: Array[String]) {

    // Create a SparkContext using every core of the local machine
    val sc = new SparkContext("local[*]", "WordCount")
    val config = ConfigFactory.load()

    val readConfig = new ReadConfig(config.getString("mongodb.db"), config.getString("mongodb.collection"), Some(config.getString("mongodb.uri")))
    val rdd = MongoSpark.load(sc, readConfig)

    val columns = extractConfig(rdd)
    val antsPos = extractAntPos(rdd)

    firstTimestamp = antsPos.first().getLong("timestamp")
    var x = 0
    for (x <- 1 to 3) {
      val currentPos = antsPos.filter(doc => filterCurrentPos(doc))
      val currentAnts = currentPos.filter(doc=>filterCurrentAnts(doc,currentPos))
      currentMillis += 1000
      currentPos.foreach(doc => println(doc))
    }

    print(columns.first().toJson())

  }

  def extractConfig(rdd: MongoRDD[Document]) = rdd.filter(doc => doc.containsKey("rows"))
  def extractAntPos(rdd: MongoRDD[Document]) = rdd.filter(doc => doc.containsKey("x"))
  def calculateMillis(): Long = if (currentMillis == 0) firstTimestamp else currentMillis

  def filterCurrentPos(doc: Document): Boolean = {
    currentMillis = calculateMillis()
    if (doc.getLong("timestamp") < currentMillis) true else false
  }
  def filterCurrentAnts(doc: Document,rdd: RDD[Document]): Boolean = {
    currentMillis = calculateMillis()
    if (doc.getLong("timestamp") < currentMillis) true else false
  }
}

