package com.mixinclass

/**
  * Created by xuaihua on 2017/2/3.
  */
class StringIterator (s: String) extends AbsIterator{
  type T = Char
  private var i = 0
  def hasNext = i < s.length()
  def next = { val ch = s charAt i; i += 1; ch }
}
object StringIteratorTest {
  def main(args: Array[String]) {
    class Iter extends StringIterator("我是9位数的字符串") with RichIterator
    val iter = new Iter
    iter foreach println
  }
}