package persistency

import javax.inject._
import java.time._
import play.api._
import play.api.db._

import models.Reminder
import helper.ReminderHelper

@Singleton
class PostgresReminderRepository @Inject()(db: Database) extends ReminderRepository {

  def allByUserId(id: Long): List[Reminder] = {
    var list: List[Reminder] = List()

    db.withConnection { conn =>
      val stmt = conn.prepareStatement("SELECT reminders.id, reminders.reminder_date, reminders.message, reminders.user_id FROM reminders JOIN users ON user_id = users.id WHERE users.id = ?")
      stmt.setLong(1, id)

      val rs = stmt.executeQuery

      while(rs.next()) {
        val reminder = ReminderHelper.createReminderFromResultSet(rs)
        list = reminder :: list
      }
    }

    return list
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
      stmt.setLong(3, reminder.reminderDate.atZone(ZoneId.of("Europe/London")).toInstant.toEpochMilli)

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

  def allBefore(date: LocalDateTime): List[Reminder] = {
    var list: List[Reminder] = List()
    val dateInMilli = date.atZone(ZoneId.of("Europe/London")).toInstant.toEpochMilli

    db.withConnection { conn =>
      val stmt = conn.prepareStatement("SELECT * FROM reminders WHERE reminder_date < ?")
      stmt.setLong(1, dateInMilli)

      val rs = stmt.executeQuery

      while (rs.next()) {
        val reminder = ReminderHelper.createReminderFromResultSet(rs) 
        list = reminder :: list
      }
    }

    return list
  }
}
