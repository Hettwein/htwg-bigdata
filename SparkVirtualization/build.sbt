name := "SparkVirtualization"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies += "com.typesafe" % "config" % "1.3.1"
libraryDependencies ++= Seq(
  "org.mongodb.spark" %% "mongo-spark-connector" % "2.0.0",
  "org.apache.spark" %% "spark-core" % "2.0.0",
  "org.apache.spark" %% "spark-sql" % "2.0.0"
)