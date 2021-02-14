package com.dixa.exercise

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging

object PrimeNumProviderApp extends App with LazyLogging {
  val conf = ConfigFactory
    .parseString("akka.http.server.preview.enable-http2 = on")
    .withFallback(ConfigFactory.defaultApplication())
    .resolve()
  val appConfig = AppConfig.load()
  val system = ActorSystem("PrimeNumbersProvider", conf)

  logger.info(s"Starting rime numbers server server...")
  new GrpcServer(system, appConfig).run()

}
