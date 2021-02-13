package com.dixa.exercise

import akka.http.scaladsl.Http
import akka.http.scaladsl.common.{EntityStreamingSupport, JsonEntityStreamingSupport}
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.util.ByteString
import com.typesafe.scalalogging.LazyLogging
import akka.NotUsed
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.common.{EntityStreamingSupport, JsonEntityStreamingSupport}
import akka.http.scaladsl.model.{HttpEntity, _}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshalling.{Marshaller, Marshalling, ToEntityMarshaller}
import akka.stream.scaladsl.Source

import scala.io.StdIn

trait ProxyRoutes extends PrimeNumStreamingSupport {
  this: LazyLogging =>

  implicit val streamingSupport = EntityStreamingSupport.json()


  //implicit val primeNumbersMarshaller: Marshaller[PrimeNumbersRequest, ByteString] =
  //  Marshaller.strict[PrimeNumbersRequest, ByteString] { t =>
  //    Marshalling.WithFixedContentType(ContentTypes.`text/html(UTF-8)`, () => ByteString.fromString(t.limit.toString))
  //}
 // val utf = MediaType.applicationWithFixedCharset("vnd.example.api.v1+json", HttpCharsets.`UTF-8`)
//
 // implicit def primeNumMarshaller: ToEntityMarshaller[R] = Marshaller.oneOf(
 //   Marshaller.withFixedContentType(ContentTypes.`text/html(UTF-8)`) { r =>
 //     HttpEntity(ContentTypes.`text/html(UTF-8)`, r.toString)
 //   })
//
 // implicit val jsonStreamingSupport: JsonEntityStreamingSupport = EntityStreamingSupport.json()

  //val grpcClient: GrpcClient

  val primeNumberRoute =
    concat(
      pathPrefix("prime" / IntNumber) { limit =>
        logger.info("received prime request")
        complete(Source.single(ByteString.apply("")))
      },
      pathPrefix("prime-json" / IntNumber) { limit =>
        logger.info("received prime json request")
        complete(Source.fromIterator(() => List(PrimeNumber(1), PrimeNumber(2), PrimeNumber(3)).iterator))
      }
    )
}
