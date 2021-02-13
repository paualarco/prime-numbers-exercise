package com.dixa.exercise

import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import com.typesafe.scalalogging.LazyLogging

import scala.io.StdIn

class ProxyRoutes {
  this: LazyLogging =>

  val route =
    concat {
      pathPrefix("prime" / IntNumber) { int =>
        logger.info("received ")

          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
      }
    }
}
