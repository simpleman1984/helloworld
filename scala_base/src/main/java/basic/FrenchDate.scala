package basic

import java.text.DateFormat._
import java.util.{Date, Locale}

/**
  * Created by xuaihua on 2017/1/20.
  */
object FrenchDate {
  def main(args: Array[String]): Unit = {
    val now = new Date
    var df = getDateInstance(LONG, Locale.FRANCE)

    println(df format now)

    println(1 + 2 * 3 / 4)
  }
}
