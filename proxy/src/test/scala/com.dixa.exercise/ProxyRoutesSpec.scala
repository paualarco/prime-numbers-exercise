package com.dixa.exercise

import akka.NotUsed
import akka.http.scaladsl.model.StatusCodes
import org.scalatest.flatspec.AnyFlatSpec
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.stream.scaladsl.Source
import com.typesafe.scalalogging.LazyLogging
import org.scalatest.matchers.should.Matchers

class ProxyRoutesSpec extends AnyFlatSpec with ScalatestRouteTest with Matchers {

  "The Proxy" should "return a greeting for GET requests to the root path" in new ProxyRoutesFixture {
    Get() ~> primeNumberRoute ~> check {
      status shouldEqual StatusCodes.OK
      responseAs[String] should startWith("<h3>Welcome to the Prime numbers provider!")
    }
  }

  it should "return a list of prime numbers" in new ProxyRoutesFixture {
    Get("/prime/10") ~> primeNumberRoute ~> check {
      status shouldEqual StatusCodes.OK
      responseAs[String] shouldEqual "[2, 3, 5, 7, 11]"
    }
  }

  it should "return bad request for non numeric prime number request" in new ProxyRoutesFixture {
    Get("/prime/notANumber") ~> primeNumberRoute ~> check {
      status shouldEqual StatusCodes.BadRequest
    }
  }

  it should "return status not found" in new ProxyRoutesFixture {
    Get("/incorrect/route") ~> primeNumberRoute ~> check {
      status shouldEqual StatusCodes.NotFound
      responseAs[String] should startWith("<h4>Resource not found")
    }
  }

  //todo figure out how to parse source streaming failures to http status codes
  //it should "handle exceptions nicely" in new ProxyRoutesFixture {
  //  override val grpcClient: GrpcClient = new GrpcClient {
  //    override def sendPrimeRequest(limit: Int): Source[PrimeNumbersResponse, NotUsed] =
  //      Source.failed(new InternalError("Grpc server not yet initialised!"))
  //  }

  //  Get("/prime/4") ~> primeNumberRoute ~> check {
  //    status shouldEqual StatusCodes.InternalServerError
  //  }
  //}

  trait ProxyRoutesFixture extends LazyLogging with ProxyRoutes {
    implicit protected val proxyConfig: ProxyConfig = ProxyConfig.load()
    protected val grpcClient: GrpcClient = new GrpcClient {
      override def sendPrimeRequest(limit: Int): Source[PrimeNumbersResponse, NotUsed] =
        Source.fromIterator(() => List(2, 3, 5, 7, 11).map(PrimeNumbersResponse(_)).iterator)
    }
  }

}
