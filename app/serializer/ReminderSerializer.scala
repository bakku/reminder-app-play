package serializer

import play.api.libs.json._
import java.time._

import models.Reminder

object ReminderSerializer {
  def serialize(reminder: Reminder): JsValue = {
    return JsObject(Map("id" -> JsNumber(reminder.id.get),
                        "message" -> JsString(reminder.message),
                        "reminderDate" -> JsString(reminder.reminderDate.toString)))
  }

  def serializeList(reminders: List[Reminder]): JsValue = reminders.foldLeft(JsArray()) { (arr, r) => arr.append(serialize(r)) }

  def deserialize(userId: Long, reminderJson: JsValue): Reminder = {
    try {
      val message = (reminderJson \ "message").as[String]
      val reminderDateString = (reminderJson \ "reminderDate").as[String]
      val reminderDate = Instant.parse(reminderDateString)

      return Reminder(userId, message, reminderDate)
    }
    catch {
      case e: JsResultException => throw new SerializeException()
    }
  }
}
