package com.dixa.exercise

import AppConfig.ServerConfiguration

import pureconfig._
import pureconfig.generic.ProductHint
import pureconfig.generic.auto._

case class AppConfig(grpcServer: ServerConfiguration)

object AppConfig {

  implicit val confHint: ProductHint[AppConfig] = ProductHint[AppConfig](ConfigFieldMapping(CamelCase, KebabCase))

  def load(): AppConfig = loadConfigOrThrow[AppConfig]

  case class ServerConfiguration(
    host: String,
    port: Int,
    endPoint: String)

}
