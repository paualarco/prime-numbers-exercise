package com.dixa.exercise

import com.typesafe.scalalogging.LazyLogging
import akka.http.scaladsl.model.{ HttpEntity, _ }
import akka.stream.scaladsl.Source
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server._
import Directives._
import akka.actor.ActorSystem

trait ProxyRoutes extends PrimeNumStreamingSupport {
  this: LazyLogging =>

  val proxyConfig: ProxyConfig
  val grpcClient: GrpcClient
  //implicit val primeNumbersMarshaller: Marshaller[Int, ByteString] =
  //  Marshaller.strict[Int, ByteString] { t =>
  //    Marshalling.WithFixedContentType(ContentTypes.`text/html(UTF-8)`, () => ByteString.fromString(t.toString))
  //  }
  implicit def grpcExceptionHandler: ExceptionHandler =
    ExceptionHandler {
      case _: io.grpc.StatusRuntimeException => {
        extractUri { uri =>
          println(s"A request to $uri failed unexpectedly.")
          complete(StatusCodes.InternalServerError, "Unexpected error.")
        }
      }
      case _: Exception =>
        extractUri { uri =>
          println(s"A request to $uri failed unexpectedly.")
          complete(StatusCodes.InternalServerError, "Unexpected error.")
        }
    }

  val primeNumberRoute =
    handleExceptions(grpcExceptionHandler) {
      concat(
        pathSingleSlash {
          complete(HttpEntity(
            ContentTypes.`text/html(UTF-8)`,
            s"""<h3>Welcome to the Prime numbers provider!</h3>
                <h3>In order to start, just pass a number to
                the path <a href=\"${proxyConfig.httpServer.endPoint}/prime/2\">/prime/n</a>
                and it will return all its prime numbers.</h3>"""))
        },
        path("null") {
          logger.info(s"Received wrong request, returning ${StatusCodes.NotFound}")
          throw new NullPointerException()
          complete(StatusCodes.OK, (HttpEntity(ContentTypes.`text/html(UTF-8)`, "Null")))
        },
        path("prime" / IntNumber) { limit =>
          get {
            println(s"Received prime numbers list request with limit of ${limit}")
            complete(grpcClient.sendPrimeRequest(limit).map(resp => PrimeNumber(resp.primeNumber)))
          }
        },
        pathPrefix("prime" / Remaining) { _ =>
          logger.info(s"Received wrong request, returning ${StatusCodes.NotFound}")
          complete(StatusCodes.BadRequest, HttpEntity(
            ContentTypes.`text/html(UTF-8)`,
            "<h4>Bad request, please the prime number limit must be of type Integer.</h4>"))
        },
        pathPrefix(Remaining) { _ =>
          logger.info(s"Received wrong request, returning ${StatusCodes.NotFound}")
          val message =
            s"""<h4>Resource not found, redirect to <a href=\"${proxyConfig.httpServer.endPoint}/prime/2\">/prime/{n}</a></h4>""".stripMargin
          complete(StatusCodes.NotFound, (HttpEntity(ContentTypes.`text/html(UTF-8)`, message)))
        })
    }

}
