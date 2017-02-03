package persistency

import java.time.Instant

import models.Reminder

trait ReminderRepository {
  def allByUserId(id: Long): List[Reminder]
  def byUserIdAndReminderId(userId: Long, reminderId: Long): Option[Reminder]
  def createForUser(userId: Long, reminder: Reminder): Long
  def deleteByReminderId(reminderId: Long)
  def allBefore(date: Instant): List[Reminder]
}
