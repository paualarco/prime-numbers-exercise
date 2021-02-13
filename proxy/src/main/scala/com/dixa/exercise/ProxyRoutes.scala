package com.dixa.exercise

import com.typesafe.scalalogging.LazyLogging
import akka.http.scaladsl.model.{HttpEntity, _}
import akka.stream.scaladsl.Source
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server._
import Directives._

trait ProxyRoutes extends PrimeNumStreamingSupport {
  this: LazyLogging =>

  val proxyConfig: ProxyConfig

  val grpcClient: GrpcClient
  //implicit val primeNumbersMarshaller: Marshaller[Int, ByteString] =
  //  Marshaller.strict[Int, ByteString] { t =>
  //    Marshalling.WithFixedContentType(ContentTypes.`text/html(UTF-8)`, () => ByteString.fromString(t.toString))
  //  }

  implicit def myExceptionHandler: ExceptionHandler =
    ExceptionHandler {
      case _: Exception =>
        extractUri { uri =>
          println(s"A request to $uri failed unexpectedly.")
          complete(HttpResponse(InternalServerError, entity = "Unexpected error."))
        }
    }

  val primeNumberRoute =
    handleExceptions(myExceptionHandler) {
      concat(
        pathSingleSlash {
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`,
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
            logger.info("received prime json request")
            complete(Source.fromIterator(() => List(PrimeNumber(1), PrimeNumber(2)).iterator))
          }
        },
        pathPrefix("prime" / Remaining) { _ =>
          logger.info(s"Received wrong request, returning ${StatusCodes.NotFound}")
          complete(StatusCodes.BadRequest, HttpEntity(ContentTypes.`text/html(UTF-8)`,
            "<h4>Bad request, please the prime number limit must be of type Integer.</h4>"))
        },
        pathPrefix(Remaining) { _ =>
          logger.info(s"Received wrong request, returning ${StatusCodes.NotFound}")
          complete(StatusCodes.NotFound, (HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h4>Resource not found, redirect to <a href=\"http://localhost:8081/prime/2\">/prime/{n}</a></h4>")))
        }
      )
    }

}
