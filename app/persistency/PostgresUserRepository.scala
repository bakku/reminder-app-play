package persistency

import javax.inject._
import play.api.db._

import models.User
import helper.UserHelper

class PostgresUserRepository @Inject()(db: Database) extends UserRepository {
  def findUserById(id: Long): Option[User] = {
    var user: User = null
    
    db.withConnection { conn =>
      val stmt = conn.prepareStatement("SELECT * FROM users WHERE id = ?")
      stmt.setLong(1, id)

      val rs = stmt.executeQuery

      if (rs.next()) {
        user = UserHelper.createUserFromResultSet(rs)
      }
    }

    return Option(user)
  }

  def findUserByEmail(email: String): Option[User] = {
    var user: User = null

    db.withConnection { conn =>
      val stmt = conn.prepareStatement("SELECT * FROM users WHERE email = ?")
      stmt.setString(1, email)

      val rs = stmt.executeQuery

      if (rs.next()) {
        user = UserHelper.createUserFromResultSet(rs)
      }
    }

    return Option(user)
  }

  def findAllUsers(): List[User] = {
    var list: List[User] = List()

    db.withConnection { conn =>
      val stmt = conn.prepareStatement("SELECT * FROM users")
      val rs = stmt.executeQuery

      while(rs.next()) {
        val user = UserHelper.createUserFromResultSet(rs)
        list = user :: list
      }
    }

    return list
  }

  def createUser(user: User) {
    db.withConnection { conn =>
      val stmt = conn.prepareStatement("INSERT INTO users(email, password, is_admin) VALUES(?, digest(?, 'sha512'), false)")
      stmt.setString(1, user.email)
      stmt.setString(2, user.password)

      stmt.executeUpdate
    }
  }

  def deleteUserById(id: Long) {
    db.withConnection { conn =>
      val stmt = conn.prepareStatement("DELETE FROM users WHERE id = ?")
      stmt.setLong(1, id)
      stmt.executeUpdate
    }
  }
}
