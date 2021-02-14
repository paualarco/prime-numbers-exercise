package com.dixa.exercise

import akka.NotUsed
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.scaladsl.Source
import com.dixa.exercise.AppConfig.ServerConfiguration
import com.typesafe.scalalogging.LazyLogging
import monix.execution.Scheduler.Implicits.global

import scala.concurrent.{ExecutionContext, Future}

class GrpcServer(actorSystem: ActorSystem, appConfig: AppConfig) extends LazyLogging {

  def run(): Future[Http.ServerBinding] = {
    // Akka boot up code
    implicit val sys: ActorSystem = actorSystem
    implicit val ec: ExecutionContext = sys.dispatcher

    // Create service handlers
    val service: HttpRequest => Future[HttpResponse] =
      PrimeProtocolHandler(new PrimeProtocolImpl())

    val ServerConfiguration(host, port, _) = appConfig.grpcServer
    // Bind service handler
    println(s"Binding grpc handler at ${appConfig.grpcServer.endPoint}")

    val binding = Http().newServerAt(host, port).bind(service)

    // report successful binding
    binding.foreach { binding => println(s"gRPC server bound to: ${binding.localAddress}") }
    binding
  }

  private class PrimeProtocolImpl extends PrimeProtocol {
    override def requestPrimeNumbers(in: PrimeNumbersRequest): Source[PrimeNumbersResponse, NotUsed] = {
      Source
        .fromPublisher(PrimeNumObservable(in.limit).toReactivePublisher)
        .map(primeNumber => new PrimeNumbersResponse(primeNumber))
    }
  }

}
