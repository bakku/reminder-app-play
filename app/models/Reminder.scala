package models

import java.time.LocalDateTime

case class Reminder(userId: Long, message: String, reminderDate: LocalDateTime, id: Option[Long] = None)
