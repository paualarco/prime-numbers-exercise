package com.dixa.exercise

import akka.NotUsed
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ HttpRequest, HttpResponse }
import akka.stream.scaladsl.Source
import com.typesafe.scalalogging.LazyLogging
import monix.execution.Scheduler.Implicits.global

import scala.concurrent.{ ExecutionContext, Future }

class GrpcServer(actorSystem: ActorSystem) extends LazyLogging {

  def run(): Future[Http.ServerBinding] = {
    // Akka boot up code
    implicit val sys: ActorSystem = actorSystem
    implicit val ec: ExecutionContext = sys.dispatcher

    // Create service handlers
    val service: HttpRequest => Future[HttpResponse] =
      PrimeProtocolHandler(new PrimeProtocolImpl())

    // Bind service handler servers to localhost:8080/8081
    val binding = Http().newServerAt("127.0.0.1", 8080).bind(service)

    // report successful binding
    binding.foreach { binding => logger.info(s"gRPC server bound to: ${binding.localAddress}") }

    binding
  }

  private class PrimeProtocolImpl extends PrimeProtocol {
    override def requestPrimeNumbers(in: PrimeNumbersRequest): Source[PrimeNumbersResponse, NotUsed] = {
      Source.fromPublisher(PrimeNumObservable(in.limit).toReactivePublisher)
        .map(primeNumber => new PrimeNumbersResponse(primeNumber))
    }
  }

}

