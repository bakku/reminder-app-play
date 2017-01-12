package services

import javax.inject._
import play.api.Logger
import play.api.libs.concurrent._
import scala.concurrent.duration._
import play.api.Play.current
import scala.concurrent.ExecutionContext.Implicits.global
import akka.actor._
import akka.pattern._
import akka.routing._

import actors.ReminderMailActor
import actors.ReminderMailActor._

@Singleton
class ReminderMailJobScheduler @Inject() (actorSystem: ActorSystem) {

  // val mailActor = actorSystem.actorOf(ReminderMailActor.props, "mail-actor")
  val mailActorRouter = actorSystem.actorOf(RoundRobinPool(5).props(ReminderMailActor.props))

  actorSystem.scheduler.schedule(0 seconds, 1 seconds) {
    mailActorRouter ! SendReminder(1)
  }

}
