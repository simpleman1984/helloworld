package com

/**
  * Created by xuaihua on 2017/2/3.
  */
class ScalaClass(var x : Int,var y:Int) {

  def hello():String = {
    x = x + 100
    y = y + 1000
    if(1==3)
      "asd"
    else
      x + "   " + y
  }

  override def toString: String =
    "(" + x + ", " + y + ")"
}