package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.db._
import play.api.libs.json._

import persistency._
import serializer.{UserSerializer, SerializeException}
import models.User
import auth.Auth

@Singleton
class UsersController @Inject()(repo: UserRepository) extends Controller {

  def index = Action { request =>
    Auth.authorizeAdmin(repo, request) match {
      case true => {
        val listOfUsers = repo.findAllUsers
        Ok(UserSerializer.serializeList(listOfUsers))
      }
      case false => {
        Forbidden("Not Authorized")
      }
    }
  }

  def show(id: Long) = Action { request =>
    repo.findUserById(id).map { user => 
      Auth.authorizeUserOrAdmin(user, repo, request) match {
        case true => Ok(UserSerializer.serialize(user))
        case false => {
          Logger.warn("Hi")
          Forbidden("Not Authorized")
        }
      }
    }.getOrElse(NotFound("User does not exist"))
  }

  def create = Action(parse.json) { request =>
    try {
      val user = UserSerializer.deserialize(request.body)
      repo.createUser(user)
      Ok("User created")
    }
    catch {
      case se: SerializeException => BadRequest("User could not be created from JSON")
    }
  }

  def delete(id: Long) = Action { request =>
    Auth.authorizeAdmin(repo, request) match {
      case true => {
        repo.deleteUserById(id)
        Ok("User deleted")
      }
      case false => Forbidden("Not Authorized")
    }
  }

}
