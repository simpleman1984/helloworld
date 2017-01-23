package basic

/**
  * Created by xuaihua on 2017/1/23.
  */
object Scalacheat {

  def main(args:Array[String]):Unit={
    var x = 5
    val y = 51

    (1 to 5).map(println)

    (1 to 5).reduceLeft(_ + _)

    val list = List(1,2,3)
    list.foreach(int0=>
      println("_____________"+int0)
    )
  }

  def f(x:Int) = {
    x*x
  }


}
