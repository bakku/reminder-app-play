package persistency

import play.api.db._

import models.User


class PostgresUserRepository(db: Database) extends UserRepository {
  def findUserById(id: Long): User = {
    var user: User = null
    
    db.withConnection { conn =>
      val stmt = conn.prepareStatement("SELECT * FROM users WHERE id = ?")
      stmt.setLong(1, id)

      val rs = stmt.executeQuery

      if (rs.next()) {
        val email = rs.getString("email")
        val password = rs.getString("password")
        val isAdmin = rs.getBoolean("is_admin")

        user = User(email, password, isAdmin)
      }
    }

    return user
  }

  def findAllUsers(): List[User] = {
    var list: List[User] = List()

    db.withConnection { conn =>
      val stmt = conn.prepareStatement("SELECT * FROM users")
      val rs = stmt.executeQuery

      while(rs.next()) {
        val email = rs.getString("email")
        val password = rs.getString("password")
        val isAdmin = rs.getBoolean("is_admin")

        val user = User(email, password, isAdmin)

        list = user :: list
      }
    }

    return list
  }
}
