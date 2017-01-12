package services

import javax.inject._
import scala.concurrent.duration._
import play.api.Configuration
import akka.actor.ActorSystem
import akka.pattern._
import akka.routing._
import play.api.libs.mailer._
import java.time.LocalDateTime
import scala.concurrent.ExecutionContext.Implicits.global

import actors.ReminderMailActor
import actors.ReminderMailActor._
import mailers.ReminderMailer
import persistency._

@Singleton
class ReminderMailJobScheduler @Inject() (actorSystem: ActorSystem, userRepo: UserRepository, reminderRepo: ReminderRepository, configuration: Configuration, mailerClient: MailerClient) {

  val mailActorRouter = actorSystem.actorOf(RoundRobinPool(5).props(ReminderMailActor.props))

  actorSystem.scheduler.schedule(0 seconds, 60 seconds) {
    val currentTime = LocalDateTime.now

    reminderRepo.allBefore(currentTime).foreach { 
      mailActorRouter ! SendReminder(_, userRepo, reminderRepo, configuration, mailerClient)
    }
  }

}
