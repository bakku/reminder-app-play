package persistency

import models.User

trait UserRepository {
  def findUserById(id: Long): Option[User]
  def findAllUsers(): List[User]
  def createUser(user: User)
  def deleteUserById(id: Long)
}
