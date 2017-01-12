package actors

import akka.actor._
import play.api.Logger

object ReminderMailActor {
  def props = Props[ReminderMailActor]

  case class SendReminder(id: Long)
}

class ReminderMailActor extends Actor with ActorLogging {
  import ReminderMailActor._

  def receive = {
    case SendReminder(id: Long) => log.warning(s"Will send reminder with id ${id.toString}. ${self.path.name}")
  }
}
