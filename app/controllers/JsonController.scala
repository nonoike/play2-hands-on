package controllers

import javax.inject.Inject

import models.Tables._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.mvc._
import slick.jdbc.H2Profile.api._
import slick.jdbc.JdbcProfile


object JsonController {
  implicit val UsersRowWritesWrites = (
    (__ \ "id").write[Long] and
      (__ \ "name").write[String] and
      (__ \ "companyId").writeNullable[Int]
    ) (unlift(UsersRow.unapply))
}

class JsonController @Inject()(val dbConfigProvider: DatabaseConfigProvider)
  extends Controller with HasDatabaseConfigProvider[JdbcProfile] {

  import JsonController._

  def list = Action.async { implicit rs =>
    db.run(Users.sortBy(t => t.id).result).map { users =>
      Ok(Json.obj("users" -> users))
    }
  }

  def create = TODO

  def update = TODO

  def remove(id: Long) = TODO
}
