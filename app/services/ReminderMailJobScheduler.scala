package services

import javax.inject._
import play.api.inject.ApplicationLifecycle
import scala.concurrent.duration._
import scala.concurrent._
import play.api.Configuration
import akka.actor.ActorSystem
import java.time.Instant
import scala.concurrent.ExecutionContext.Implicits.global
import com.rabbitmq.client._

import actors.ReminderMailActor
import actors.ReminderMailActor._
import mailers.ReminderMailer
import persistency._

@Singleton
class ReminderMailJobScheduler @Inject() (lifecycle: ApplicationLifecycle, actorSystem: ActorSystem, reminderRepo: ReminderRepository) {//, userRepo: UserRepository, reminderRepo: ReminderRepository, configuration: Configuration, mailerClient: MailerClient) {

  val queueName = "hello"
  val factory = new ConnectionFactory()
  factory.setHost("localhost")
  val connection = factory.newConnection()
  val channel = connection.createChannel()
  channel.queueDeclare(queueName, false, false, false, null)

  actorSystem.scheduler.schedule(0 seconds, 10 seconds) {
    val currentTime = Instant.now

    reminderRepo.allBefore(currentTime).foreach { reminder =>
      val message = reminder.userId.toString + "," + reminder.id.get.toString
      channel.basicPublish("", queueName, null, message.getBytes)
    }
  }
}
