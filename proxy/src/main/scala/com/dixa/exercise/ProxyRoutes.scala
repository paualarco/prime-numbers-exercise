package com.dixa.exercise

import com.typesafe.scalalogging.LazyLogging
import akka.http.scaladsl.model.{HttpEntity, _}
import akka.http.scaladsl.server._
import Directives._
import akka.http.scaladsl.common.EntityStreamingSupport
import akka.http.scaladsl.marshalling.{Marshaller, ToEntityMarshaller}

trait ProxyRoutes { //extends PrimeNumStreamingSupport {
  this: LazyLogging =>

  val proxyConfig: ProxyConfig
  val grpcClient: GrpcClient

  private[this] implicit def grpcExceptionHandler: ExceptionHandler =
    ExceptionHandler {
      case _: Exception =>
        extractUri { uri =>
          logger.error(s"A request to $uri failed unexpectedly.")
          complete(StatusCodes.InternalServerError, "Unexpected error.")
        }
    }

  private[this] implicit val marshaller: ToEntityMarshaller[PrimeNumbersResponse] =
    Marshaller.oneOf(Marshaller.withFixedContentType(ContentTypes.`text/plain(UTF-8)`) { primeNumbersResponse ⇒
      HttpEntity.apply(primeNumbersResponse.primeNumber.toString)
    })

  private[this] implicit val streamingSupport = new PrimeNumbersResponseStreamingSupport()

  val primeNumberRoute =
    concat(
      pathSingleSlash {
        complete(
          HttpEntity(
            ContentTypes.`text/html(UTF-8)`,
            s"""<h3>Welcome to the Prime numbers provider!</h3>
                <h3>In order to start, just pass a number to
                the path <a href=\"${proxyConfig.httpServer.endPoint}/prime/2\">/prime/n</a>
                and it will return all its prime numbers.</h3>"""
          ))
      },
      handleExceptions(grpcExceptionHandler) {
        path("prime" / IntNumber) { limit =>
          get {
            logger.info(s"Received prime numbers list request with limit of ${limit}")
            complete(grpcClient.sendPrimeRequest(limit))
          }
        }
      },
      pathPrefix("prime" / Remaining) { _ =>
        logger.info(s"Received wrong request, returning ${StatusCodes.NotFound}")
        complete(
          StatusCodes.BadRequest,
          HttpEntity(
            ContentTypes.`text/html(UTF-8)`,
            "<h4>Bad request, please the prime number limit must be of type Integer.</h4>"))
      },
      pathPrefix(Remaining) { _ =>
        logger.info(s"Received wrong request, returning ${StatusCodes.NotFound}")
        val message =
          s"""<h4>Resource not found, redirect to <a href=\"${proxyConfig.httpServer.endPoint}/prime/2\">/prime/{n}</a></h4>""".stripMargin
        complete(StatusCodes.NotFound, (HttpEntity(ContentTypes.`text/html(UTF-8)`, message)))
      }
    )

}
