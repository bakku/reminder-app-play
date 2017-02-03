package models

import java.time.Instant

case class Reminder(userId: Long, message: String, reminderDate: Instant, id: Option[Long] = None)

