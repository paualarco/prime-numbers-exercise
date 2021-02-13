package com.dixa.exercise.config

import com.dixa.exercise.PrimeNumServerConfig
import com.dixa.exercise.PrimeNumServerConfig.ServerConfiguration
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class PrimeNumServerConfigSpec extends AnyFlatSpec with Matchers {

  it should "be configurable from `application.conf`" in {
    //given / when
    val primeNumConf = PrimeNumServerConfig.load()

    primeNumConf.grpcServer shouldBe ServerConfiguration("localhost", 9090, "http://localhost:9090")
  }
}
