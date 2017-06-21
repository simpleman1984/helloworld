package com.mixinclass

/**
  * Created by xuaihua on 2017/2/3.
  */
abstract  class AbsIterator {
  type T
  def hasNext: Boolean
  def next: T
}
