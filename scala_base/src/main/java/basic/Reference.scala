package basic

/**
  * Created by xuaihua on 2017/1/23.
  */
class Reference[T] {
  private var contents: T = _

  def set(value: T) {
    contents = value
  }

  def get: T = contents
}
