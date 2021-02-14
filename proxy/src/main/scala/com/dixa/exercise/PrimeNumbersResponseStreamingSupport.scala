package com.dixa.exercise

import akka.NotUsed
import akka.http.scaladsl.common.EntityStreamingSupport
import akka.http.scaladsl.model.{ContentType, ContentTypeRange, ContentTypes}
import akka.stream.scaladsl.Flow
import akka.util.ByteString
import akka.http.javadsl.model.{ContentTypeRange => JContentTypeRange, ContentType => JContentType}

private[dixa] final class PrimeNumbersResponseStreamingSupport() extends EntityStreamingSupport {

  override def supported: ContentTypeRange = ContentTypeRange(ContentTypes.`text/plain(UTF-8)`)

  override def contentType: ContentType = ContentTypes.`text/plain(UTF-8)`

  override def framingDecoder: Flow[ByteString, ByteString, NotUsed] =
    Flow[ByteString].intersperse(ByteString("["), ByteString(","), ByteString("]"))

  override def framingRenderer: Flow[ByteString, ByteString, NotUsed] =
    Flow[ByteString].intersperse(ByteString("["), ByteString(", "), ByteString("]"))

  override def parallelism: Int = 10

  override def unordered: Boolean = false

  override def withSupported(range: JContentTypeRange): EntityStreamingSupport = this

  override def withContentType(range: JContentType): EntityStreamingSupport = this

  override def withParallelMarshalling(parallelism: Int, unordered: Boolean): EntityStreamingSupport = this
}
