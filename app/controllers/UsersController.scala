package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.db._
import play.api.libs.json._

import persistency._
import serializer.{UserSerializer, SerializeException}
import models.User

@Singleton
class UsersController @Inject()(repo: UserRepository) extends Controller {

  def index = Action {
    val listOfUsers = repo.findAllUsers
    Ok(UserSerializer.serializeList(listOfUsers))
  }

  def show(id: Long) = Action {
    repo.findUserById(id).map { user => 
      Ok(UserSerializer.serialize(user))
    }.getOrElse(NotFound)
  }

  def create = Action(parse.json) { request =>
    try {
      val user = UserSerializer.deserialize(request.body)
      repo.createUser(user)
      Ok
    }
    catch {
      case se: SerializeException => BadRequest("User could not be created from JSON")
    }
  }

  def delete(id: Long) = Action {
    repo.deleteUserById(id)
    Ok
  }

}
