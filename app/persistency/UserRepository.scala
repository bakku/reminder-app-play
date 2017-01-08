package persistency

import models.User

trait UserRepository {
  def findUserById(id: Long): User
  def findAllUsers(): List[User]
}
