package serializer

import play.api.libs.json._

import models.User

object UserSerializer {
  def serialize(user: User): JsValue = {
    return JsObject(Map("id" -> JsNumber(user.id.get),
                        "email" -> JsString(user.email)))
  }

  def serializeList(users: List[User]): JsValue = {
    var usersAsJson = JsArray()
    
    users.foreach { u =>
      usersAsJson = usersAsJson.append(serialize(u))
    }

    return usersAsJson
  }

  def deserialize(userJson: JsValue): User = {
    try {
      val email = (userJson \ "email").as[String]
      val password = (userJson \ "password").as[String]

      return User(email, password, false)
    }
    catch {
      case e: JsResultException => throw new SerializeException()
    }
  }
}
