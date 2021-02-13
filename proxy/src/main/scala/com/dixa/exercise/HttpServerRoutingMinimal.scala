package com.dixa.exercise


import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.ExecutionContext.Implicits.global
import scala.io.StdIn

object HttpServerRoutingMinimal extends ProxyRoutes with LazyLogging {

  def main(args: Array[String]): Unit = {

    implicit val system = ActorSystem("my-system")
    // needed for the future flatMap/onComplete in the end


    val bindingFuture = Http().newServerAt("localhost", 8081).bind(primeNumberRoute)

    println(s"Server online at http://localhost:8081/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}