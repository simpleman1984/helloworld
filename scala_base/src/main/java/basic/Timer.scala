package basic

/**
  * Created by xuaihua on 2017/1/23.
  */
object Timer {
  def oncePerSecond(callback: () => Unit): Unit = {
    while (true) {
      callback()
      Thread sleep 1000
    }
  }

  def timeFlies() {
    println("time flies like an arrow...")
  }


  def main(args: Array[String]): Unit = {
    oncePerSecond(timeFlies)
  }
}
