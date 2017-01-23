package basic

/**
  * Created by xuaihua on 2017/1/23.
  */
object ComplexNumbers {

  def main(args: Array[String]): Unit = {
    val c = new Complex(1.2, 3.4)
    println("imaginary part : " + c.xx())

    val ref = new Reference[String];
    ref.set("我是谁")
    println(ref.get)
    println(c.toString)

    val javaTest = new JavaTest
    val result = javaTest.hello("你是谁,,,,,,,,,,,,,,")
    println(result)
  }

}
