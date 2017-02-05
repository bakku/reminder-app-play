package helper

import java.net.URI
import com.rabbitmq.client._

object RabbitMQHelper {
  def setupConnection(rabbitmqUri: String): Connection = {
    val factory = new ConnectionFactory()

    val uri = new URI(rabbitmqUri)

    factory.setUsername(uri.getUserInfo.split(":")(0));
    factory.setPassword(uri.getUserInfo.split(":")(1));
    factory.setHost(uri.getHost);
    factory.setPort(uri.getPort);
    factory.setVirtualHost(uri.getPath.substring(1));

    val connection = factory.newConnection()
 
    return connection
  }
}
