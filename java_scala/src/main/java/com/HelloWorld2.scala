package com

import scala.collection.mutable

/**
  * Created by xuaihua on 2017/2/3.
  */
object HelloWorld2 {
  def main(args:Array[String]):Unit = {
    println("who am i")

    val set = new mutable.LinkedHashSet[Any]
    set += "this is a string"
    set += 732
    set += 'c'

    val iter : Iterator[Any] = set.iterator

    while(iter.hasNext)
    {
        println(iter.next())
    }
  }

  def hello():String = {
    "test"
  }

}
