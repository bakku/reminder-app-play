package modules

import com.google.inject.AbstractModule

import services._

class SchedulerModule extends AbstractModule {
  
  override def configure() = {
    bind(classOf[ReminderMailJobScheduler]).asEagerSingleton()
  }

}

