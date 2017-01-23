package basic

/**
  * Created by xuaihua on 2017/1/23.
  */
object TimerAnonymous {
  def oncePerSecond(callback: () => Unit): Unit = {
    while (true) {
      callback();
      Thread.sleep(1000)
    }
  }

  def main(args: Array[String]): Unit = {
    oncePerSecond(() =>
      println("time fliese like an arrow")
    )
  }
}
