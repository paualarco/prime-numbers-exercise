package com.dixa.exercise

import cats.effect.{ContextShift, ExitCode, IO, IOApp}
import com.typesafe.scalalogging.LazyLogging
import monix.eval.{Task, TaskApp}
import org.http4s.server.blaze.BlazeServerBuilder
import sun.rmi.server.Dispatcher

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object ProxyApp extends  with LazyLogging {

  implicit val config: ProxyConfig = ProxyConfig.load()

  logger.info(s"Starting master server with config: $config")


}
