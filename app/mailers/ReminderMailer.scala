package mailers

import javax.inject._
import play.api.libs.mailer._

object ReminderMailer {
  def constructMail(from: String, to: String, message: String): Email = {
    val textTemplate = """Hi
                         |
                         |You wanted to get reminded right now of the following message:
                         |
                         |{message}
                         |
                         |I hope I was of help
                         |
                         |Best regards,
                         |
                         |Your Reminder""".stripMargin

    val emailTemplate = """<p>
                          |Hi!<br><br>
                          |
                          |You wanted to get reminded right now of the following message:<br><br>
                          |
                          |{message}<br><br>
                          |
                          |I hope I was of help<br><br>
                          |
                          |Best regards,<br><br>
                          |
                          |Your Reminder
                          |</p>""".stripMargin

    Email(
      "Your reminder",
      "Reminder App <" + from + ">",
      Seq(to + "<" + to + ">"),
      bodyText = Some(textTemplate.replace("{message}", message)),
      bodyHtml = Some(emailTemplate.replace("{message}", message))
    )
  }
}
