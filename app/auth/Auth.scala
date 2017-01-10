package auth

import javax.inject._
import play.api._
import play.api.mvc.{Request, AnyContent}
import com.roundeights.hasher.Implicits._

import persistency.UserRepository
import helper.Base64Helper
import models.User

object Auth {
  
  def authorizeAdmin(userRepo: UserRepository, request: Request[Any]): Boolean = {
    val auth = request.headers.get("Authorization").getOrElse(None)   

    auth match {
      case None => false
      case encodedString: String => {
        val credentials = Base64Helper.decodeAuthHeader(encodedString)

        userRepo.findUserByEmail(credentials("email")).map { user =>
          // remove \x from beginning of postgres hash
          val normalizedPass = user.password.substring(2)
          val normalizedHeaderPass = credentials("password").sha512.hex

          normalizedPass.equals(normalizedHeaderPass) && user.isAdmin
        }.getOrElse(false)
      }
    }
  }

  def authorizeUserOrAdmin(allowedUser: User, userRepo: UserRepository, request: Request[Any]): Boolean = {
    authorizeAdmin(userRepo, request) match {
      case true => true
      case false => {
        val auth = request.headers.get("Authorization").getOrElse(None)

        auth match {
          case None => false
          case encodedString: String => {
            val credentials = Base64Helper.decodeAuthHeader(encodedString)

            userRepo.findUserByEmail(credentials("email")).map { user =>
              // remove \x from beginning of postgres hash
              val normalizedPass = user.password.substring(2)
              val normalizedHeaderPass = credentials("password").sha512.hex

              allowedUser.email.equals(user.email) && normalizedPass.equals(normalizedHeaderPass)
            }.getOrElse(false)
          }
        }
      }
    }
  }

}
