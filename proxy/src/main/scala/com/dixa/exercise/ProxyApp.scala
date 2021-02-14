package com.dixa.exercise

import akka.actor.ActorSystem
import com.typesafe.scalalogging.LazyLogging
import akka.http.scaladsl.Http
import com.dixa.exercise.ProxyConfig.ServerConfiguration

import scala.concurrent.Await
import scala.concurrent.duration._

object ProxyApp extends App with ProxyRoutes with LazyLogging {

  implicit val actorSystem = ActorSystem("proxyApp")
  implicit val proxyConfig: ProxyConfig = ProxyConfig.load()

  protected val grpcClient: GrpcClient = new PrimesProtocolGrpcClient()

  private[this] val ServerConfiguration(host, port, _) = proxyConfig.httpServer

  val binding = Http().newServerAt(host, port).bind(primeNumberRoute)
  // once ready to terminate the server, invoke terminate:

  logger.info(s"Starting server at ${proxyConfig.httpServer.endPoint}...")

  scala.sys.addShutdownHook {
    logger.info("Terminating proxy...")
    actorSystem.terminate()
    Await.result(actorSystem.whenTerminated, 10.seconds)
    logger.info("Terminated actor system.")
  }

}
