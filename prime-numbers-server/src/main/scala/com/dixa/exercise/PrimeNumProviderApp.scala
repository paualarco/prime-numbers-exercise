package com.dixa.exercise

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

object PrimeNumProviderApp extends App {
  val conf = ConfigFactory
    .parseString("akka.http.server.preview.enable-http2 = on")
    .withFallback(ConfigFactory.defaultApplication())
  val system = ActorSystem("PrimeNumbersProvider")
  new GrpcServer(system).run()
}