import com.google.inject.AbstractModule

import persistency._

class Module extends AbstractModule {

  override def configure() = {
    bind(classOf[UserRepository]).to(classOf[PostgresUserRepository])
  }

}
