package services

import javax.inject._
import play.api.inject.ApplicationLifecycle
import scala.concurrent.duration._
import scala.concurrent._
import play.api.Configuration
import akka.actor.ActorSystem
import akka.pattern._
import akka.routing._
import play.api.libs.mailer._
import java.time.LocalDateTime
import scala.concurrent.ExecutionContext.Implicits.global
import com.rabbitmq.client._

import actors.ReminderMailActor
import actors.ReminderMailActor._
import mailers.ReminderMailer
import persistency._

@Singleton
class ReminderMailJobScheduler @Inject() (lifecycle: ApplicationLifecycle, actorSystem: ActorSystem) {//, userRepo: UserRepository, reminderRepo: ReminderRepository, configuration: Configuration, mailerClient: MailerClient) {

  //val mailActorRouter = actorSystem.actorOf(RoundRobinPool(5).props(ReminderMailActor.props))

  val queueName = "hello"
  val factory = new ConnectionFactory()
  factory.setHost("localhost")
  val connection = factory.newConnection()
  val channel = connection.createChannel()
  channel.queueDeclare(queueName, false, false, false, null)

  actorSystem.scheduler.schedule(0 seconds, 10 seconds) {
    //val currentTime = LocalDateTime.now

    //reminderRepo.allBefore(currentTime).foreach { 
    //  mailActorRouter ! SendReminder(_, userRepo, reminderRepo, configuration, mailerClient)
    //}
    val message = "Hello world"
    channel.basicPublish("", queueName, null, message.getBytes)
  }

  lifecycle.addStopHook { () =>
    Future.successful {
      channel.close
      connection.close
    }
  }
}
