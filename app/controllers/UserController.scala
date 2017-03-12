package controllers


import javax.inject.Inject

import models.Tables._
import play.api.data.Forms._
import play.api.data._
import play.api.db.slick._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc._
import slick.jdbc.H2Profile.api._
import slick.jdbc.JdbcProfile

import scala.concurrent.Future

object UserController {

  case class UserForm(id: Option[Long], name: String, companyId: Option[Int])

  val userForm = Form(
    mapping(
      "id" -> optional(longNumber),
      "name" -> nonEmptyText(maxLength = 20),
      "companyId" -> optional(number)
    )(UserForm.apply)(UserForm.unapply)
  )
}

class UserController @Inject()(val dbConfigProvider: DatabaseConfigProvider,
                               val messagesApi: MessagesApi)
  extends Controller with HasDatabaseConfigProvider[JdbcProfile] with I18nSupport {

  import UserController._

  def list = Action.async { implicit rs =>
    db.run(Users.sortBy(t => t.id).result).map {
      users => Ok(views.html.user.list(users))
    }
  }

  def edit(id: Option[Long]) = Action.async { implicit rs =>
    val form = if (id.isDefined) {
      db.run(Users.filter(t => t.id === id.get.bind).result.head).map { user =>
        UserController.userForm.fill(UserForm(Some(user.id), user.name, user.companyId))
      }
    } else {
      Future {
        userForm
      }
    }

    form.flatMap { form =>
      db.run(Companies.sortBy(_.id).result).map { companies =>
        Ok(views.html.user.edit(form, companies))
      }
    }
  }

  def create = TODO

  def update = TODO

  def remove(id: Long) = TODO

}
