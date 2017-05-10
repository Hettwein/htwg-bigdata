package de.htwg.bigdata.visual

import com.mongodb.spark.MongoSpark
import com.mongodb.spark.config.ReadConfig
import com.typesafe.config
import com.typesafe.config.ConfigFactory
import org.bson.Document
import com.mongodb.spark.rdd.MongoRDD

object WordCount {
  import org.apache.spark._
  import org.apache.spark.SparkContext._
  import org.apache.log4j._
  import org.apache.spark.sql.SparkSession

  /** Count up how many of each word appears in a book as simply as possible. */

  /** Our main function where the action happens */
  def main(args: Array[String]) {

    // Create a SparkContext using every core of the local machine
    val sc = new SparkContext("local[*]", "WordCount")
    val config = ConfigFactory.load()
    
    val readConfig = new ReadConfig(config.getString("mongodb.db"), config.getString("mongodb.collection"), Some(config.getString("mongodb.uri")))
    val rdd = MongoSpark.load(sc, readConfig)
    
    val columns=extractConfig(rdd)
    val ants=extractAnts(rdd)
    
    print(columns.first().toJson())

  }
  
  def extractConfig(rdd:MongoRDD[Document])=rdd.filter(doc=>doc.containsKey("rows"))
  def extractAnts(rdd:MongoRDD[Document])=rdd.filter(doc=>doc.containsKey("x"))

}