package mailers

import javax.inject._
import play.api.libs.mailer._

object ReminderMailer {
  def constructMail(from: String, to: String, message: String): Email = {
    Email(
      "Your reminder",
      "Reminder App <" + from + ">",
      Seq(to + "<" + to + ">"),
      bodyText = Some(message)
    )
  }
}
