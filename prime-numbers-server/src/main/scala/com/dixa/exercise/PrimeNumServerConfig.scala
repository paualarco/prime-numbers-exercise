package com.dixa.exercise

import PrimeNumServerConfig.ServerConfiguration

import pureconfig._
import pureconfig.generic.ProductHint
import pureconfig.generic.auto._

case class PrimeNumServerConfig(grpcServer: ServerConfiguration)

object PrimeNumServerConfig {

  implicit val confHint: ProductHint[PrimeNumServerConfig] = ProductHint[PrimeNumServerConfig](ConfigFieldMapping(CamelCase, KebabCase))

  def load(): PrimeNumServerConfig = loadConfigOrThrow[PrimeNumServerConfig]

  case class ServerConfiguration(
    host: String,
    port: Int,
    endPoint: String)

}
