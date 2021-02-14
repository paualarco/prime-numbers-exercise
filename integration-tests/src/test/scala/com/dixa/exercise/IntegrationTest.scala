package com.dixa.exercise

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ ContentTypes, HttpEntity, MediaTypes, StatusCodes }
import org.scalatest.flatspec.AnyFlatSpec
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.stream.scaladsl.Sink
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers

import scala.concurrent.Await
import scala.concurrent.duration._

class IntegrationTest extends AnyFlatSpec with ScalatestRouteTest with Matchers with ScalaFutures {

  val httpEndpoint = "http://localhost:8080"
  private[this] val httpClient = Http()

  val parse: String => String = (str: String) =>
    str.replace("[", "").replace("]", "")

  "A Get '/prime/10'" should "return all its prime numbers" in {
    val get = Get(s"$httpEndpoint/prime/18")

    val expectedPrimeNumbers = """{"n":2},{"n":3},{"n":5},{"n":7},{"n":11},{"n":13},{"n":17}""".stripMargin

    val foldSink = Sink.fold[String, String]("")((left, right) => left ++ parse(right))

    //when
    val f = httpClient.singleRequest(get)

    val result = Await.result(f, 5.seconds)
    result.status shouldBe StatusCodes.OK
    result.entity.contentType shouldBe ContentTypes.`application/json`
    result.entity.dataBytes
      .map(_.utf8String)
      .runWith(foldSink)
      .futureValue shouldBe expectedPrimeNumbers
  }

  "A Get to '/prime/non-int'" should "return bad request" in {
    val get = Get(s"$httpEndpoint/prime/a")

    //when
    val f = httpClient.singleRequest(get)

    val result = Await.result(f, 5.seconds)
    result.status shouldBe StatusCodes.BadRequest
  }

  "A Get to no-where" should "return resource not found" in {
    val get = Get(s"$httpEndpoint/no/where")

    //when
    val f = httpClient.singleRequest(get)

    val result = Await.result(f, 5.seconds)
    result.status shouldBe StatusCodes.NotFound
  }

}
