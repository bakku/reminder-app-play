package models


case class User(email: String, password: String, isAdmin: Boolean, id: Option[Long] = None)

