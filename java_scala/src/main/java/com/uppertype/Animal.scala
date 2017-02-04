package com.uppertype

abstract class Animal {
  def name: String
}

abstract class Pet extends Animal {}

class Cat extends Pet {
  override def name: String = "Cat"
}
class Dog extends Pet {
  override def name: String = "Dog"
}
class Lion extends Animal {
  override def name: String = "Lion"
}
class Cage[P <: Pet](p: P) {
  def pet: P = p
}
object Main extends App {
  var dogCage = new Cage[Dog](new Dog)
  var catCage = new Cage[Cat](new Cat)
  /* Cannot put Lion in a cage as Lion is not a Pet. */
  //  var lionCage = new Cage[Lion](new Lion)
}