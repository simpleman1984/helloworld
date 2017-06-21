package com

/**
  * Created by xuaihua on 2017/2/3.
  */
object ScalaTest {

  def main(args:Array[String]):Unit={
    println("scala 调用java示例")

    val helloWorld = new HelloWorld

    val ret = helloWorld.hello("scala 传入的字符串 ");

    println(ret)

    val scalaClass = new ScalaClass(1,3)
    val scalaRet = scalaClass.hello()

    println("scala类的返回值 : " + scalaRet)
  }

}
