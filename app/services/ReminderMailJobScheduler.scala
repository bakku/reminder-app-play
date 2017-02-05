package services

import java.net.URI
import javax.inject._
import scala.concurrent.duration._
import scala.concurrent._
import akka.actor.ActorSystem
import java.time.Instant
import scala.concurrent.ExecutionContext.Implicits.global
import com.rabbitmq.client._
import play.Configuration

import persistency._
import helper.RabbitMQHelper

@Singleton
class ReminderMailJobScheduler @Inject() (actorSystem: ActorSystem, reminderRepo: ReminderRepository, config: Configuration) {
  val queueName = "reminder"

  val connection = RabbitMQHelper.setupConnection(config.getString("cloudamqp_url"))

  val channel = connection.createChannel()

  val durable = true
  channel.queueDeclare(queueName, durable, false, false, null)

  actorSystem.scheduler.schedule(0 seconds, 10 seconds) {
    val currentTime = Instant.now

    reminderRepo.allBefore(currentTime).foreach { reminder =>
      val message = reminder.userId.toString + "," + reminder.id.get.toString
      channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes)
    }
  }
}
