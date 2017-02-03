package modules

import com.google.inject.AbstractModule

import services._

class WorkerModule extends AbstractModule {
	
	override def configure() = {
		bind(classOf[ReminderMailWorkerPool]).asEagerSingleton()
	}

}

