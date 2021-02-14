package com.dixa.exercise

import com.dixa.exercise.PrimeNumObservable.isPrime
import org.scalatest.flatspec.AnyFlatSpec
import monix.execution.Scheduler.Implicits.global
import org.scalatest.matchers.should.Matchers

class PrimeNumObservableSpec extends AnyFlatSpec with Matchers {


  it should "emit all prime numbers until the specified limit" in {
    //given
    val expected = List(
      2, 3, 5, 7, 11,
      13, 17, 19, 23,
      29, 31, 37, 41,
      43, 47, 53, 59,
      61, 67, 71, 73,
      79, 83, 89, 97)

    //when
    val primes = PrimeNumObservable(100).toListL.runSyncUnsafe()

    //then
    expected shouldBe primes
  }

  it should "only emit subsequent prime numbers including the limit" in {
    //given
    val expected = List(2, 3, 5, 7)

    //when
    val primes = PrimeNumObservable(7).toListL.runSyncUnsafe()

    //then
    expected shouldBe primes
  }

  "isPrime" should "only return true on prime numbers" in {
    //given
    val expected = List(
      2, 3, 5, 7, 11,
      13, 17, 19, 23,
      29, 31, 37, 41,
      43, 47, 53, 59,
      61, 67, 71, 73,
      79, 83, 89, 97)

    //when
    val primes = (1 to 100).filter(isPrime)

    //then
    expected shouldBe primes
  }

  it should "return false on non prime numbers" in {
    //given
    val expected = List(0, 1, 4, 6)

    //when
    val primes = (0 to 7).filterNot(isPrime)

    //then
    expected shouldBe primes
  }

}
