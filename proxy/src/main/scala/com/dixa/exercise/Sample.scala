package com.dixa.exercise

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport

class JsonStreamingFullExamples {


  //#custom-content-type
  import akka.NotUsed
  import akka.actor.ActorSystem
  import akka.http.scaladsl.Http
  import akka.http.scaladsl.common.{ EntityStreamingSupport, JsonEntityStreamingSupport }
  import akka.http.scaladsl.model.{ HttpEntity, _ }
  import akka.http.scaladsl.server.Directives._
  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  import akka.http.scaladsl.marshalling.{ Marshaller, ToEntityMarshaller }
  import akka.stream.scaladsl.Source

  import scala.io.StdIn
  import scala.util.Random

  final case class User(name: String, id: String)

  trait MyTweetJsonProtocol
    extends SprayJsonSupport with spray.json.DefaultJsonProtocol {

    case class Tweet(uid: Int, txt: String)

    implicit val tweetFormat = jsonFormat2(Tweet.apply)
  }

  object ApiServer extends App with MyTweetJsonProtocol {
    implicit val system = ActorSystem("api")
    implicit val executionContext = system.dispatcher

    implicit val jsonStreamingSupport: JsonEntityStreamingSupport = EntityStreamingSupport.json()


    val route =
      path("tweets") {
        // [3] simply complete a request with a source of tweets:
        val tweets: Source[Tweet, NotUsed] = Source.single(Tweet(1, ""))
        complete(tweets)
      }

  }

  //#custom-content-type
}