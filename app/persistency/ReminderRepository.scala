package persistency

import models.Reminder

trait ReminderRepository {
  def allByUserId(id: Long): List[Reminder]
}
