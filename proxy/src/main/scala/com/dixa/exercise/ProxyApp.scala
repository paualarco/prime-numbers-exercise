package com.dixa.exercise

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ContentTypes
import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.server.Directives.{complete, path}
import com.typesafe.scalalogging.LazyLogging
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import scala.io.StdIn

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.{Failure, Success}

object ProxyApp extends ProxyRoutes with LazyLogging  {

  implicit val system = ActorSystem("my-system")
  implicit val config: ProxyConfig = ProxyConfig.load()

  val grpcClient: GrpcClient = new GrpcClient()

  def main(args: Array[String]): Unit = {

    logger.info(s"Starting master server with config: $config")
    val route =
      path("hello") {
        Directives.get {
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
        }
      }
    val bindingFuture = Http().newServerAt("localhost", 8082).bind(route)

    logger.info(s"Prime number routes running at http://localhost:8080/\nPress RETURN to stop...")
    bindingFuture
      .onComplete {
        case Success(value) => {
          println("Server finishing gracefully!!")
         system.terminate()
        }
        case Failure(ex) => println("Exception encountered!! " + ex)
      }
        // and shutdown when done
  }


}
