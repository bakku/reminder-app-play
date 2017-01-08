package serializer

import play.api.libs.json._

import models.User

object UserSerializer {
  def convert(user: User): JsValue = {
    implicit val userWrites = Json.writes[User]
    return Json.toJson(user)
  }  

  def convertList(users: List[User]): JsValue = {
    var usersAsJson = JsArray()
    
    users.foreach { u =>
      usersAsJson = usersAsJson.append(JsObject(Map("email" -> JsString(u.email),
                                                    "password" -> JsString(u.password),
                                                    "isAdmin" -> JsBoolean(u.isAdmin))))
    }

    return usersAsJson
  }
}
