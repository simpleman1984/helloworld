package com.extractor

/**
  * Created by xuaihua on 2017/2/3.
  */
object Twice {
  def apply(x: Int): Int = x * 2
  def unapply(z: Int): Option[Int] = if (z%2 == 0) Some(z/2) else None
}
object TwiceTest extends App {
  val x = Twice(21)
  println(x)

  x match { case Twice(n) => Console.println(n) } // prints 21
}