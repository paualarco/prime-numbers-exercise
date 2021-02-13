package com.dixa.exercise

import akka.actor.ActorSystem
import com.typesafe.scalalogging.LazyLogging
import akka.http.scaladsl.Http
import com.dixa.exercise.ProxyConfig.ServerConfiguration

import scala.util.{Failure, Success}

object ProxyApp extends App with ProxyRoutes with LazyLogging {

  implicit val actorSystem = ActorSystem("proxyApp")
  implicit val proxyConfig: ProxyConfig = ProxyConfig.load()

  val grpcClient: GrpcClient = new PrimesProtocolGrpcClient()

  val ServerConfiguration(host, port, _) = proxyConfig.httpServer

  Http().newServerAt(host, port).bind(primeNumberRoute)
  println(s"Starting server at ${proxyConfig.httpServer.endPoint}...")
}
