import com.google.inject.AbstractModule

import java.time.Clock
import persistency._

class Module extends AbstractModule {

  override def configure() = {
    bind(classOf[Clock]).toInstance(Clock.systemDefaultZone)

    bind(classOf[UserRepository]).to(classOf[PostgresUserRepository])

    bind(classOf[ReminderRepository]).to(classOf[PostgresReminderRepository])
  }

}
