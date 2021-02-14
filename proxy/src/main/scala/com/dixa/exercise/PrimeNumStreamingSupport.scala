package com.dixa.exercise

import akka.http.scaladsl.common.EntityStreamingSupport
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport

trait PrimeNumStreamingSupport extends SprayJsonSupport with spray.json.DefaultJsonProtocol {
  case class PrimeNumber(n: Int)
  implicit val streamingSupport = EntityStreamingSupport.json()
  implicit val primeNumberJsonFormat = jsonFormat1(PrimeNumber.apply)
}