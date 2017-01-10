package serializer

import play.api.libs.json._
import java.time._

import models.Reminder

object ReminderSerializer {
  def serialize(reminder: Reminder): JsValue = {
    return JsObject(Map("id" -> JsNumber(reminder.id.get),
                        "message" -> JsString(reminder.message),
                        "reminderDate" -> JsNumber(reminder.reminderDate.atZone(ZoneId.of("Europe/London")).toInstant.toEpochMilli)))
  }

  def serializeList(reminders: List[Reminder]): JsValue = {
    var remindersAsJson = JsArray()

    reminders.foreach { r =>
      remindersAsJson = remindersAsJson.append(serialize(r))
    }

    return remindersAsJson
  }

  def deserialize(userId: Long, reminderJson: JsValue): Reminder = {
    try {
      val message = (reminderJson \ "message").as[String]
      val reminderDateMillis = (reminderJson \ "reminderDate").as[Long]
      val reminderDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(reminderDateMillis), ZoneId.of("Europe/London"))
      
      return Reminder(userId, message, reminderDate)
    }
    catch {
      case e: JsResultException => throw new SerializeException()
    }
  }
}
