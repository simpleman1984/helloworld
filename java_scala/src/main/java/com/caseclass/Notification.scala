package com.caseclass

/**
  * Created by xuaihua on 2017/2/3.
  */
abstract  class Notification
  case class Email(sourceEmail: String, title: String, body: String) extends Notification
  case class SMS(sourceNumber: String, message: String) extends Notification
  case class VoiceRecording(contactName: String, link: String) extends Notification

object NotificationTest extends App
{
  val emailFromJohn = Email("john.doe@mail.com", "Greetings From John!", "Hello World!")
  val title = emailFromJohn.title

  println(title)

  val editedEmail = emailFromJohn.copy(title = "I am learning Scala!", body = "It's so cool!")

  println(emailFromJohn)
  println(editedEmail)

  val firstSms = SMS("12345", "Hello!")
  val secondSms = SMS("12345", "Hello!")
  if (firstSms == secondSms) {
    println("They are equal!")
  }
  println("SMS is: " + firstSms)

  //弹出显示各种通知消息
  def showNotification(notification: Notification): String = {
    notification match {
      case Email(email, title, _) =>
        "You got an email from " + email + " with title: " + title
      case SMS(number, message) =>
        "You got an SMS from " + number + "! Message: " + message
      case VoiceRecording(name, link) =>
        "you received a Voice Recording from " + name + "! Click the link to hear it: " + link
    }
  }

  val someSms = SMS("12345", "Are you there?")
  val someVoiceRecording = VoiceRecording("Tom", "voicerecording.org/id/123")
  println(showNotification(someSms))
  println(showNotification(someVoiceRecording))

  def matchTest(x: Int): String = x match {
    case 1 => "one"
    case 2 => "two"
    case _ => "many"
  }
  println(matchTest(3))

  def matchAny(x: Any): Any = x match {
    case 1 => "one"
    case "two" => 2
    case y: Int => "scala.Int"
  }
  println(matchAny("two"))

}