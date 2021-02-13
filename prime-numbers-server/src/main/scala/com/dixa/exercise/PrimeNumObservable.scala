package com.dixa.exercise

import com.dixa.exercise.PrimeNumObservable.isPrime
import monix.eval.Task
import monix.execution.{ Ack, Cancelable }
import monix.reactive.Observable
import monix.reactive.observers.Subscriber

class PrimeNumObservable(limit: Int) extends Observable[Int] {

  override def unsafeSubscribeFn(subscriber: Subscriber[Int]): Cancelable = {
    val s = subscriber.scheduler
    feedWithPrimeNumbers(subscriber).runToFuture(s)
  }

  def feedWithPrimeNumbers(sub: Subscriber[Int], current: Int = 2): Task[Unit] = {
    if (isPrime(current)) {
      Task.deferFuture(sub.onNext(current))
        .flatMap {
          case Ack.Continue => feedWithPrimeNumbers(sub, current + 1)
          case Ack.Stop => Task.unit
        }
    } else if (current < limit) feedWithPrimeNumbers(sub, current + 1)
    else Task.evalOnce(sub.onComplete())
  }

}

object PrimeNumObservable {

  private[dixa] def apply(limit: Int): PrimeNumObservable = new PrimeNumObservable(limit)

  private[dixa] def isPrime(i: Int): Boolean =
    if (i <= 1) false
    else if (i == 2) true
    else !(2 until i).exists(n => i % n == 0)
}
