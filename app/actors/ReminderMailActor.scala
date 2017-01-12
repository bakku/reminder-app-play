package actors

import akka.actor._
import play.api.db._
import play.api.Configuration
import play.api.libs.mailer._

import persistency._
import models._
import mailers._

object ReminderMailActor {
  def props = Props[ReminderMailActor]

  case class SendReminder(reminder: Reminder, userRepo: UserRepository, reminderRepo: ReminderRepository, configuration: Configuration, mailerClient: MailerClient)
}

class ReminderMailActor extends Actor {
  import ReminderMailActor._

  def receive = {
    case SendReminder(reminder: Reminder, userRepo: UserRepository, reminderRepo: ReminderRepository, configuration: Configuration, mailerClient: MailerClient) => {
      val user = userRepo.findUserById(reminder.userId)
      val email = ReminderMailer.constructMail(
        configuration.underlying.getString("play.mailer.user"),
        user.get.email,
        reminder.message
      )
      mailerClient.send(email)
      reminderRepo.deleteByReminderId(reminder.id.get)
    }
  }
}
