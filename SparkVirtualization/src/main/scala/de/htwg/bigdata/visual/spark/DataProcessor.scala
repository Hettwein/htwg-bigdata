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
import com.fasterxml.jackson._
import scala.xml.Group
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import java.io.StringWriter

class DataProcessor extends java.io.Serializable {
  case class GridRepresentation(step: Int, time: Long, fields: Array[Document])

  def transformGrid(gridRequest: GridRequest): String = {

    // Create a SparkContext using every core of the local machine
    val sc = new SparkContext("local[*]", "DataProcessor")
    val config = ConfigFactory.load()

    val readConfig = new ReadConfig(config.getString("mongodb.db"), gridRequest.collection, Some(config.getString("mongodb.uri")))
    val rdd = MongoSpark.load(sc, readConfig)

    val columns = extractConfig(rdd)
    val antsPos = extractAntPos(rdd)

    //(3,123)
    //(3,222)  <---
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
      //current Pos for each timestep
      val currentPos = antsPos.filter(doc => filterCurrentPos(doc, currentMillis))

      // sort by timestamp, reduce by id
      //(3,123)
      //(3,222)  <---
      val currentAnts = currentPos
        .map(doc => (doc.getLong("timestamp").toInt, doc))
        .sortByKey(false, 1)
        .map(t => ((t._2.getString("id").toInt, t._2)))
        .reduceByKey((a: Document, b: Document) => a)

      currentAnts.foreach(doc => println("current Ants:" + doc))

      //calculate new x and y
      // newX=x*ratio
      val transormedGrid = currentAnts.map(t =>
        {
          var doc = t._2
          doc.append("newX", (doc.getInteger("x") * ratio).toInt)
          doc.append("newY", (doc.getInteger("y") * ratio).toInt)
          doc.append("posID", (doc.get("newX").toString() + "_" + doc.get("newY").toString()))
        }).map(doc => (doc.get("posID").toString().hashCode(), doc))
        .reduceByKey((doc: Document, doc2: Document) => doc)

      //(id,count)
      val antCount = transormedGrid.countByKey()

      //calculate concentration
      val newGrid = transormedGrid.map(doc => {
        doc._2.append("ants", (antCount.get(doc._1)) match {
          case Some(x: Long) => x // this extracts the value in a as an Int
          case _             => 0L
        })
        var conc = doc._2.getLong("ants").toFloat / antNumber * 100
        doc._2.append("concentration", conc)
      })

      //build json
      val step = GridRepresentation(stepCount, currentMillis, newGrid.collect())
      gridRepresentation ::= step

      currentMillis += gridRequest.timestep
      stepCount = stepCount + 1
    } while (currentMillis <= lastTimestamp)

    return parseToJson(gridRepresentation)
  }

  private def parseToJson(gridRep: List[GridRepresentation]): String = {

    val mapper = new ObjectMapper()
    mapper.registerModule(DefaultScalaModule)
    val out = new StringWriter
    mapper.writeValue(out, gridRep)

    val json = out.toString()
    return json
  }

  private def extractConfig(rdd: MongoRDD[Document]) = rdd.filter(doc => doc.containsKey("rows"))
  private def extractAntPos(rdd: MongoRDD[Document]) = rdd.filter(doc => doc.containsKey("x"))
  private def filterCurrentPos(doc: Document, currentMillis: Long): Boolean = if (doc.getLong("timestamp") < currentMillis) true else false

  object TimeOrdering extends Ordering[Document] {
    def compare(doca: Document, docb: Document) = doca.getLong("timestamp").toInt compare docb.getLong("timestamp").toInt
  }
}