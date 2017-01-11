package modules

import com.google.inject.AbstractModule

import services._

class JobModule extends AbstractModule {
  
  override def configure() = {
    bind(classOf[ApplicationTimer]).asEagerSingleton()
  }

}
