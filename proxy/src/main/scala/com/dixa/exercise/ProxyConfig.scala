package com.dixa.exercise

import ProxyConfig.{GrpcServerConfiguration, HttpServerConfiguration, ServerConfiguration}

import java.time.format.DateTimeFormatter.ISO_DATE
import scala.concurrent.duration.FiniteDuration
import ProxyConfig.{GrpcServerConfiguration, HttpServerConfiguration}
import io.circe._
import io.circe.generic.auto._
import io.circe.generic.semiauto._
import io.circe.syntax._
import pureconfig._
import pureconfig.configurable.localDateConfigConvert
import pureconfig.generic.ProductHint
import pureconfig.generic.auto._

case class ProxyConfig(httpServer: ServerConfiguration, grpcTimeout: FiniteDuration, grpcServer: ServerConfiguration) {
  def toJson: String = this.asJson.noSpaces
}

object ProxyConfig {

  implicit val confHint: ProductHint[ProxyConfig] = ProductHint[ProxyConfig](ConfigFieldMapping(CamelCase, KebabCase))

  def load(): ProxyConfig = loadConfigOrThrow[ProxyConfig]

  case class ServerConfiguration(
    host: String,
    port: Int,
    endPoint: String)

}

