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

object WordCount {

  /** Count up how many of each word appears in a book as simply as possible. */

  /** Our main function where the action happens */
  def main(args: Array[String]) {
  transformGrid(new GridRequest("collection7",80,80,1000))
    
  }
  
  def transformGrid(gridRequest:GridRequest) = {
    // Create a SparkContext using every core of the local machine
    val sc = new SparkContext("local[*]", "WordCount")
    val config = ConfigFactory.load()

    val readConfig = new ReadConfig(config.getString("mongodb.db"), config.getString("mongodb.collection"), Some(config.getString("mongodb.uri")))
    val rdd = MongoSpark.load(sc, readConfig)

    val columns = extractConfig(rdd)
    val antsPos = extractAntPos(rdd)

    val firstTimestamp=antsPos.min()(TimeOrdering).getLong("timestamp")
    val lastTimestamp=antsPos.max()(TimeOrdering).getLong("timestamp")
    var currentMillis=firstTimestamp
    do{
      val currentPos = antsPos.filter(doc => filterCurrentPos(doc,currentMillis))
      val currentAnts = currentPos.filter(doc => filterCurrentAnts(doc, currentPos,currentMillis))
      currentMillis += gridRequest.timestep
      currentPos.foreach(doc => println(doc))
    }while(currentMillis<lastTimestamp)


  } 
  
  //as

  def extractConfig(rdd: MongoRDD[Document]) = rdd.filter(doc => doc.containsKey("rows"))
  def extractAntPos(rdd: MongoRDD[Document]) = rdd.filter(doc => doc.containsKey("x"))
  def filterCurrentPos(doc: Document,currentMillis:Long): Boolean = if (doc.getLong("timestamp") < currentMillis) true else false
  
  def filterCurrentAnts(doc: Document, rdd: RDD[Document],currentMillis:Long): Boolean = if (doc.getLong("timestamp") < currentMillis) true else false
  
  
  object TimeOrdering extends Ordering[Document]{
    def compare(doca:Document, docb:Document) = doca.getLong("timestamp").toInt compare docb.getLong("timestamp").toInt
  }
  
}

