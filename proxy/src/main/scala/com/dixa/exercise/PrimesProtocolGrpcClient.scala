package com.dixa.exercise

import akka.NotUsed
import akka.actor.ActorSystem
import akka.grpc.GrpcClientSettings
import akka.stream.scaladsl.Source
import com.dixa.exercise.ProxyConfig.ServerConfiguration
import com.typesafe.scalalogging.LazyLogging

class PrimesProtocolGrpcClient(implicit proxyConfig: ProxyConfig, actorSystem: ActorSystem)
  extends GrpcClient with LazyLogging { self =>

  logger.info(s"Starting grpc server on endpoint: ${proxyConfig.grpcServer.endPoint}")

  private[this] val ServerConfiguration(grpcServerHost, grpcServerPort, _) = proxyConfig.grpcServer
  private[this] val clientSettings =
    GrpcClientSettings.connectToServiceAt(grpcServerHost, grpcServerPort).withTls(false)
  private[this] val client: PrimeProtocol = PrimeProtocolClient(clientSettings)

  protected def sendPrimeRequest(limit: Int): Source[PrimeNumbersResponse, NotUsed] = {
    client.requestPrimeNumbers(new PrimeNumbersRequest(limit))
  }
}

trait GrpcClient {
  def sendPrimeRequest(limit: Int): Source[PrimeNumbersResponse, NotUsed]
}
