package com.func

/**
  * Created by xuaihua on 2017/2/3.
  */
object Func extends App{
  var t2 = () => { System.getProperty("user.dir") }

  val t = (x: Int, y: Int) => "(" + x + ", " + (y + 100) + ")"

  println(t2())
  println(t(1,3))

  def apply(f:Int=>String,v:Int) = f(v)

  class Decorator(left:String,right:String){
    def layout[A](x:A) = left+x.toString() + right
  }

  val decorator = new Decorator("[","]")
  println(apply(decorator.layout,791))

}
