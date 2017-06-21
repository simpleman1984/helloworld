package com.mixinclass

/**
  * Created by xuaihua on 2017/2/3.
  */
trait RichIterator extends AbsIterator{
  def foreach(f: T => Unit) { while (hasNext) f(next) }
}
