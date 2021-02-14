package com.dixa.exercise

import ProxyConfig.ServerConfiguration

import scala.concurrent.duration.FiniteDuration
import pureconfig._
import pureconfig.generic.ProductHint
import pureconfig.generic.auto._

case class ProxyConfig(httpServer: ServerConfiguration, grpcServer: ServerConfiguration)

object ProxyConfig {

  implicit val confHint: ProductHint[ProxyConfig] = ProductHint[ProxyConfig](ConfigFieldMapping(CamelCase, KebabCase))

  def load(): ProxyConfig = loadConfigOrThrow[ProxyConfig]

  case class ServerConfiguration(host: String, port: Int, endPoint: String)

}
