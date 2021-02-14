package com.dixa.exercise

import com.dixa.exercise.ProxyConfig.ServerConfiguration
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ProxyConfigSpec extends AnyFlatSpec with Matchers {

  it should "be configurable from `application.conf`" in {
    // given/when
    val primeNumConf = ProxyConfig.load()

    // then
    primeNumConf.httpServer shouldBe ServerConfiguration("localhost", 8080, "http://localhost:8080")
    primeNumConf.grpcServer shouldBe ServerConfiguration("localhost", 9090, "http://localhost:9090")
  }

}
