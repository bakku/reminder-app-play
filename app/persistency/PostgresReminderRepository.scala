package persistency

import javax.inject._
import java.time._
import play.api._
import play.api.db._
import java.sql.ResultSet

import models.Reminder
import helper.ReminderHelper

@Singleton
class PostgresReminderRepository @Inject()(db: Database) extends ReminderRepository {

  def allByUserId(id: Long): List[Reminder] = {
    db.withConnection { conn =>
      val stmt = conn.prepareStatement("SELECT reminders.id, reminders.reminder_date, reminders.message, reminders.user_id FROM reminders JOIN users ON user_id = users.id WHERE users.id = ?")
      stmt.setLong(1, id)
      val rs = stmt.executeQuery

      buildReminderList(rs, List())
    }
  }

  def byUserIdAndReminderId(userId: Long, reminderId: Long): Option[Reminder] = {
    db.withConnection { conn =>
      val stmt = conn.prepareStatement("SELECT reminders.id, reminders.reminder_date, reminders.message, reminders.user_id FROM reminders JOIN users ON user_id = users.id WHERE users.id = ? AND reminders.id = ?")
      stmt.setLong(1, userId)
      stmt.setLong(2, reminderId)

      val rs = stmt.executeQuery

      if (rs.next()) {
        Some(ReminderHelper.createReminderFromResultSet(rs))
      }
      else {
        None
      }
    }
  }

  def createForUser(userId: Long, reminder: Reminder): Long = {
    db.withConnection { conn =>
      val stmt = conn.prepareStatement("INSERT INTO reminders(user_id, message, reminder_date) VALUES(?, ?, ?) RETURNING id")
      stmt.setLong(1, userId)
      stmt.setString(2, reminder.message)
      stmt.setLong(3, reminder.reminderDate.toEpochMilli)

      val rs = stmt.executeQuery
      rs.next()
      return rs.getLong(1)
    }
  }

  def deleteByReminderId(reminderId: Long) {
    db.withConnection { conn =>
      val stmt = conn.prepareStatement("DELETE FROM reminders WHERE id = ?")
      stmt.setLong(1, reminderId)

      stmt.executeUpdate
    }
  }

  def allBefore(date: Instant): List[Reminder] = {
    val dateInMilli = date.toEpochMilli

    db.withConnection { conn =>
      val stmt = conn.prepareStatement("SELECT * FROM reminders WHERE reminder_date < ?")
      stmt.setLong(1, dateInMilli)

      val rs = stmt.executeQuery
      buildReminderList(rs, List())
    }
  }


  private def buildReminderList(rs: ResultSet, list: List[Reminder]): List[Reminder] = {
    if (rs.next()) {
      return buildReminderList(rs, list :+ ReminderHelper.createReminderFromResultSet(rs))
    }
    else {
      return list
    }
  }
}
