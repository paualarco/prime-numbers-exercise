package com.dixa.exercise

import com.dixa.exercise.AppConfig.ServerConfiguration
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class AppConfigSpec extends AnyFlatSpec with Matchers {

  it should "be configurable from `application.conf`" in {
    //given / when
    val primeNumConf = AppConfig.load()

    primeNumConf.grpcServer.host shouldBe "localhost"
    primeNumConf.grpcServer.port shouldBe 9090
    primeNumConf.grpcServer shouldBe ServerConfiguration("localhost", 9090, "http://localhost:9090")
  }
}
