package basic

import java.util.Date

/**
  * Created by xuaihua on 2017/1/23.
  */
class Complex(real: Double, imaginary: Double) {
  def re() = real

  def im() = imaginary

  def xx() = {
    val result = real * imaginary
    result
  }

  override def toString: String = "real__________" + imaginary;
}
