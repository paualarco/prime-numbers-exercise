package com.dixa.exercise

import akka.NotUsed
import akka.http.javadsl.model.{ContentType, ContentTypeRange}
import akka.http.scaladsl.common.{EntityStreamingSupport, JsonEntityStreamingSupport}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{ContentType, ContentTypeRange}
import akka.stream.scaladsl.Flow
import akka.util.ByteString

trait PrimeNumStreamingSupport extends SprayJsonSupport with spray.json.DefaultJsonProtocol {
  case class PrimeNumber(number: Int)
  implicit val tweetFormat = jsonFormat1(PrimeNumber.apply)
}
