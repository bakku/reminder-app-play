package helper

import java.sql.ResultSet

import models.User

object UserHelper {
  
  def createUserFromResultSet(rs: ResultSet): User = {
    val id = rs.getLong("id")
    val email = rs.getString("email")
    val password = rs.getString("password")
    val isAdmin = rs.getBoolean("is_admin")

    return User(email, password, isAdmin, Some(id))
  }

}
