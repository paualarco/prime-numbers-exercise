package com.dixa.exercise

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ ContentTypes, StatusCodes }
import org.scalatest.flatspec.AnyFlatSpec
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.stream.scaladsl.Sink
import org.scalatest.BeforeAndAfterAll
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers

import scala.concurrent.{ Await, Future }
import scala.concurrent.duration._

class IntegrationTest extends AnyFlatSpec with ScalatestRouteTest with Matchers with ScalaFutures with BeforeAndAfterAll {

  val httpEndpoint = "http://localhost:8080"
  private[this] val httpClient = Http()

  override implicit val patienceConfig = PatienceConfig(20.seconds, 100.milliseconds)

  override def beforeAll() = {
    Future.sequence(List.fill(5)(Get(s"$httpEndpoint/prime/2")).map(httpClient.singleRequest(_))).futureValue
    super.beforeAll()
  }

  val parse: String => String = (str: String) =>
    str.replace("[", "").replace("]", "")

  "A Get '/prime/10'" should "return all its prime numbers" in {
    //given
    val get = Get(s"$httpEndpoint/prime/18")
    val expectedPrimeNumbers = """2, 3, 5, 7, 11, 13, 17""".stripMargin
    val foldSink = Sink.fold[String, String]("")((left, right) => left ++ parse(right))

    //when/then
    val result = httpClient.singleRequest(get).futureValue
    result.status shouldBe StatusCodes.OK
    result.entity.contentType shouldBe ContentTypes.`text/plain(UTF-8)`
    result.entity.dataBytes
      .map(_.utf8String)
      .runWith(foldSink)
      .futureValue shouldBe expectedPrimeNumbers
  }

  "A Get to '/prime/non-int'" should "return bad request" in {
    //given
    val get = Get(s"$httpEndpoint/prime/a")

    //when
    val f = httpClient.singleRequest(get)

    //then
    val result = Await.result(f, 10.seconds)
    result.status shouldBe StatusCodes.BadRequest
  }

  "A Get to no-where" should "return resource not found" in {
    //given
    val get = Get(s"$httpEndpoint/no/where")

    //when
    val f = httpClient.singleRequest(get)

    //then
    val result = Await.result(f, 5.seconds)
    result.status shouldBe StatusCodes.NotFound
  }

}
