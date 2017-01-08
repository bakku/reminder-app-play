package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.db._

import persistency.PostgresUserRepository
import serializer.UserSerializer
import models.User

@Singleton
class UsersController @Inject()(db: Database) extends Controller {

  def index = Action {
    val listOfUsers = new PostgresUserRepository(db).findAllUsers
    Ok(UserSerializer.convertList(listOfUsers))
  }

  def show(id: Long) = Action {
    val user = new PostgresUserRepository(db).findUserById(id)
 
    if (user == null) {
      NotFound
    }
    else {
      Ok(UserSerializer.convert(user))
    }
  }
}
