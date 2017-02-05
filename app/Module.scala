import com.google.inject.AbstractModule
import java.time.Clock
import play.api.libs.concurrent.AkkaGuiceSupport

import persistency._

class Module extends AbstractModule {

  override def configure() = {
    bind(classOf[UserRepository]).to(classOf[PostgresUserRepository])

    bind(classOf[ReminderRepository]).to(classOf[PostgresReminderRepository])
  }

}
