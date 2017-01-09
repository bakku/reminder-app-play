package persistency

import javax.inject._
import play.api._
import play.api.db._

import models.Reminder

@Singleton
class PostgresReminderRepository @Inject()(db: Database) extends ReminderRepository {

  def allByUserId(id: Long): List[Reminder] = {
    return List()
  }

}
