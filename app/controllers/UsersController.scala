package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.db._
import play.api.libs.json._

import persistency.PostgresUserRepository
import models.User

@Singleton
class UsersController @Inject()(db: Database) extends Controller {

  def show(id: Long) = Action {
    //Ok(views.html.index("Your new application is ready."))
    implicit val userWrites = Json.writes[User]
 
    val user = new PostgresUserRepository(db).findUserById(id)
 
    if (user == null) {
      NotFound
    }
    else {
      val userAsJson = Json.toJson(user)
      Ok(userAsJson)
    }
  }

}
