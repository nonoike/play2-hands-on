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

import scala.concurrent.Future


object JsonController {

  private val AUTO_INCREMENT_COLUMN_DUMMY_VALUE = 0
  implicit val UsersRowWritesWrites = (
    (__ \ "id").write[Long] and
      (__ \ "name").write[String] and
      (__ \ "companyId").writeNullable[Int]
    ) (unlift(UsersRow.unapply))

  implicit val UserFormFormat = (
    (__ \ "id").readNullable[Long] and
      (__ \ "name").read[String] and
      (__ \ "companyId").readNullable[Int]
    ) (UserForm)

  case class UserForm(id: Option[Long], name: String, companyId: Option[Int])

}

class JsonController @Inject()(val dbConfigProvider: DatabaseConfigProvider)
  extends Controller with HasDatabaseConfigProvider[JdbcProfile] {

  import JsonController._

  def list = Action.async { implicit rs =>
    db.run(Users.sortBy(t => t.id).result).map { users =>
      Ok(Json.obj("users" -> users))
    }
  }

  def create = Action.async(parse.json) { implicit rs =>
    rs.body.validate[UserForm].map { form =>
      val user = UsersRow(AUTO_INCREMENT_COLUMN_DUMMY_VALUE, form.name, form.companyId)
      db.run(Users += user).map { _ =>
        Ok(Json.obj("result" -> "success"))
      }
    }.recoverTotal { e =>
      Future {
        BadRequest(Json.obj("result" -> "failure", "error" -> JsError.toJson(e)))
      }
    }
  }

  def update = Action.async(parse.json) { implicit rs =>
    rs.body.validate[UserForm].map { form =>
      val user = UsersRow(form.id.get, form.name, form.companyId)
      db.run(Users.filter(t => t.id === user.id.bind).update(user)).map { _ =>
        Ok(Json.obj("result" -> "success"))
      }
    }.recoverTotal { e =>
      Future {
        BadRequest(Json.obj("result" -> "failure", "error" -> JsError.toJson(e)))
      }
    }
  }

  def remove(id: Long) = TODO
}
