package controllers

import javax.inject.Inject

import play.api.db.slick._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import slick.jdbc.JdbcProfile


class UserController @Inject()(val dbConfigProvider: DatabaseConfigProvider,
                               val messageApi: MessagesApi) extends Controller
  with HasDatabaseConfigProvider[JdbcProfile] with I18nSupport {

  def list = TODO

  def edit(id: Option[Long]) = TODO

  def create = TODO

  def update = TODO

  def remove(id: Long) = TODO

  override def messagesApi: MessagesApi = ???
}
