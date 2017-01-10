package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.db._
import play.api.libs.json._

import persistency.{UserRepository, ReminderRepository}
import serializer.{ReminderSerializer, SerializeException}
import auth.Auth

@Singleton
class RemindersController @Inject()(userRepo: UserRepository, reminderRepo: ReminderRepository) extends Controller {

  def index(userId: Long) = UserOrAdminAction(userId, userRepo) { request =>
    val reminders = reminderRepo.allByUserId(userId)
    Ok(ReminderSerializer.serializeList(reminders))
  }

  def create(userId: Long) = Action(parse.json) { request =>
    userRepo.findUserById(userId).map { user =>
      Auth.authorizeUserOrAdmin(user, userRepo, request) match {
        case true => {
          try {
            val reminder = ReminderSerializer.deserialize(userId, request.body)
            val id = reminderRepo.createForUser(userId, reminder)
            Ok("Reminder created").withHeaders(LOCATION -> ("/users/" + userId + "/reminders/" + id))
          }
          catch {
            case se: SerializeException => BadRequest("Reminder could not be created from JSON")
          }
        }
        case false => Forbidden("Not authorized")
      }
    }.getOrElse(NotFound("Not found"))
  }

  def show(userId: Long, reminderId: Long) = UserOrAdminAction(userId, userRepo) { request =>
    val reminder = reminderRepo.byUserIdAndReminderId(userId, reminderId)
    reminder.map { r => Ok(ReminderSerializer.serialize(r)) }.getOrElse(NotFound("Not found"))
  }

  def delete(userId: Long, reminderId: Long) = UserOrAdminAction(userId, userRepo) { request =>
    reminderRepo.deleteByReminderId(reminderId)
    Ok("Reminder deleted")
  }

  def UserOrAdminAction(userId: Long, userRepo: UserRepository)(block: Request[AnyContent] => Result) = Action { request =>
    userRepo.findUserById(userId).map { user =>
      Auth.authorizeUserOrAdmin(user, userRepo, request) match {
        case true => block(request)
        case false => Forbidden("Not authorized")
      }
    }.getOrElse(NotFound("Not found"))
  }

}
