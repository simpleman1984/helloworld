package com.trait0

/**
  * Created by xuaihua on 2017/2/3.
  */
trait Similarity {
  def isSimilar(x: Any): Boolean
  def isNotSimilar(x: Any): Boolean = !isSimilar(x)
}
