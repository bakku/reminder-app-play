package helper

import java.sql.ResultSet
import java.time._

import models.Reminder

object ReminderHelper {
  
  def createReminderFromResultSet(rs: ResultSet): Reminder = {
    val userId = rs.getLong("user_id")
    val message = rs.getString("message")
    val reminderDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(rs.getLong("reminder_date")), ZoneId.of("Europe/London"))
    val id = rs.getLong("id")

    return Reminder(userId, message, reminderDate, Some(id))
  }

}
