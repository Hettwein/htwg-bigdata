package computerdatabase

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import net.liftweb.json._
import spray.json.{DefaultJsonProtocol, _}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods.POST
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import scala.concurrent.ExecutionContextExecutor

case class Ant_DTO(id: String, x_current: Int, y_current: Int, x_new: Int, y_new: Int)

class BasicSimulation extends Simulation with DefaultJsonProtocol {

  implicit val ant_dtoFormat = jsonFormat5(Ant_DTO)
  implicit val system = ActorSystem()
  implicit val executor = system.dispatcher
  implicit val materializer = ActorMaterializer()
  val headers_10 = Map("Content-Type" -> """application/json""")
  var maybeId: Option[String] = Option("0")
  var maybeX: Option[Int] = Option(0)
  var maybeY: Option[Int] = Option(0)

  Http().singleRequest(HttpRequest(uri = "http://192.168.99.113:27020/newsimulation", entity = ""))

  val httpConf = http
    .baseURL("http://192.168.99.113:27020") // Here is the root for all relative URLs
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  val scn = scenario("Ants Test") // A scenario is a chain of requests and pauses
//    .exec(http("start simulation")
//    .get("/newsimulation"))
//    .pause(1) // Note that Gatling has recorder real time pauses
    .exec(http("create ant")
      .post("/ant")
      .check(jsonPath("$.id")
      .saveAs("id"))
      .check(jsonPath("$.x_current")
      .saveAs("current_x"))
      .check(jsonPath("$.y_current")
      .saveAs("current_y")))
    .pause(1)
    .exec(session => {
      maybeId = session.get("id").asOption[String]
      println(maybeId.getOrElse("COULD NOT FIND ID"))
      maybeX = session.get("current_x").asOption[Int]
      println(maybeX.getOrElse("COULD NOT FIND X"))
      maybeY = session.get("current_y").asOption[Int]
      println(maybeY.getOrElse("COULD NOT FIND Y"))
      session
    })
    .exec(http("move ant1")
      .put("/ant/" + maybeId.get)
      .headers(headers_10)
      .body(StringBody(Ant_DTO(maybeId.get, maybeX.get, maybeY.get, maybeX.get + 1, maybeY.get + 1).toJson.toString())))
//    .exec(http("move ant2")
//      .put("/ant/1")
//      .headers(headers_10)
//      .body(StringBody(Ant_DTO("1", 2, 2, 3, 3).toJson.toString())))
//    .exec(http("move ant3")
//      .put("/ant/1")
//      .headers(headers_10)
//      .body(StringBody(Ant_DTO("1", 3, 3, 4, 4).toJson.toString())))
//    .exec(http("move ant4")
//      .put("/ant/1")
//      .headers(headers_10)
//      .body(StringBody(Ant_DTO("1", 4, 4, 5, 5).toJson.toString())))
//    .exec(http("move ant5")
//      .put("/ant/1")
//      .headers(headers_10)
//      .body(StringBody(Ant_DTO("1", 5, 5, 6, 6).toJson.toString())))

  //    .pause(3)
  //    .exec(http("request_4")
  //      .get("/"))
  //    .pause(2)
  //    .exec(http("request_5")
  //      .get("/computers?p=1"))
  //    .pause(670 milliseconds)
  //    .exec(http("request_6")
  //      .get("/computers?p=2"))
  //    .pause(629 milliseconds)
  //    .exec(http("request_7")
  //      .get("/computers?p=3"))
  //    .pause(734 milliseconds)
  //    .exec(http("request_8")
  //      .get("/computers?p=4"))
  //    .pause(5)
  //    .exec(http("request_9")
  //      .get("/computers/new"))
  //    .pause(1)
  //    .exec(http("request_10") // Here's an example of a POST request
  //      .post("/computers")
  //      .formParam("""name""", """Beautiful Computer""") // Note the triple double quotes: used in Scala for protecting a whole chain of characters (no need for backslash)
  //      .formParam("""introduced""", """2012-05-30""")
  //      .formParam("""discontinued""", """""")
  //      .formParam("""company""", """37"""))

  setUp(scn.inject(atOnceUsers(1)).protocols(httpConf))
}
