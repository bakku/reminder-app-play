package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.db._
import play.api.libs.json._

import persistency.{UserRepository, ReminderRepository}
@Singleton
class RemindersController @Inject()(repo: UserRepository) extends Controller {

  def index(userId: Long) = Action {
    Ok("hi") 
  }

}
