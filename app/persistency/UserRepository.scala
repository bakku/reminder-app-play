package persistency

import models.User

trait UserRepository {
  def findUserById(id: Long): Option[User]
  def findUserByEmail(email: String): Option[User]
  def findAllUsers(): List[User]
  def createUser(user: User): Long
  def deleteUserById(id: Long)
}
