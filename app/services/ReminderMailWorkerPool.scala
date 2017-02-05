package services

import javax.inject._
import play.api.inject.ApplicationLifecycle
import com.rabbitmq.client._
import play.Configuration
import play.api.Logger
import play.api.libs.mailer._
import akka.actor.ActorSystem
import scala.concurrent.Future

import persistency._
import mailers._
import helper.RabbitMQHelper

@Singleton
class ReminderMailWorkerPool @Inject() (lifecycle: ApplicationLifecycle, reminderRepo: ReminderRepository, userRepo: UserRepository, config: Configuration, mailerClient: MailerClient, akkaSystem: ActorSystem) {

  val queueName = "reminder"

	val conn = RabbitMQHelper.setupConnection(config.getString("cloudamqp_url"))

  val workerContext = akkaSystem.dispatchers.lookup("worker")
  val amountThreads = sys.env("WORKER_THREADS").toInt
  
  (1 to amountThreads) foreach { _ =>
    Future {
      initializeNewWorker
    }(workerContext)
  }

  private def initializeNewWorker {
    val channel = conn.createChannel

    val durable = true
    channel.queueDeclare(queueName, durable, false, false, null)

    val consumer = new DefaultConsumer(channel) {
      override def handleDelivery(consumerTag: String, envelope: Envelope, properties: AMQP.BasicProperties, body: Array[Byte]) {
        val message = new String(body, "UTF-8")
        val userId = message.split(",")(0).toInt
        val reminderId = message.split(",")(1).toInt

        val user = userRepo.findUserById(userId)
        val reminder = reminderRepo.byUserIdAndReminderId(userId, reminderId)

        val mail = ReminderMailer.constructMail(config.getString("play.mailer.user"), user.get.email, reminder.get.message)
        mailerClient.send(mail)

        reminderRepo.deleteByReminderId(reminderId)
        Logger.debug(s"sent user: ${user.get.email}, reminder: ${reminder.get.message}")
        channel.basicAck(envelope.getDeliveryTag, false)
      }
    }

    val autoAck = false
    channel.basicConsume(queueName, autoAck, consumer)
  }
}

