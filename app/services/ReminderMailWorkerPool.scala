package services

import javax.inject._
import play.api.inject.ApplicationLifecycle
import com.rabbitmq.client._

@Singleton
class ReminderMailWorkerPool @Inject() (lifecycle: ApplicationLifecycle) {

  val queueName = "hello"
  val factory = new ConnectionFactory()
  factory.setHost("localhost")

	val conn = factory.newConnection
	val channel = conn.createChannel

	channel.queueDeclare(queueName, false, false, false, null)

	val consumer = new DefaultConsumer(channel) {
		override def handleDelivery(consumerTag: String, envelope: Envelope, properties: AMQP.BasicProperties, body: Array[Byte]) {
			val message = new String(body, "UTF-8")
			println(message)
		}
	}

	channel.basicConsume(queueName, true, consumer)
}

