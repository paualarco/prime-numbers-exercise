package com.dixa.exercise

import akka.NotUsed
import akka.actor.ActorSystem
import akka.grpc.GrpcClientSettings
import akka.stream.scaladsl.Source
import com.typesafe.scalalogging.LazyLogging

class GrpcClient(implicit config: ProxyConfig, actorSystem: ActorSystem) extends LazyLogging { self =>

  logger.info(s"Starting grpc server on endpoint: ${config.grpcServer.endPoint}")

  val clientSettings = GrpcClientSettings.connectToServiceAt("127.0.0.1", 8080).withTls(false)

  val client: PrimeProtocol = PrimeProtocolClient(clientSettings)

  def sendPrimeRequest(limit: Int): Source[PrimeNumbersResponse, NotUsed] = {
    client.requestPrimeNumbers(new PrimeNumbersRequest(limit))
  }
}
