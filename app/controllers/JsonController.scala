package controllers

import javax.inject.Inject

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.mvc._
import slick.jdbc.JdbcProfile

class JsonController @Inject()(val dbConfigProvider: DatabaseConfigProvider)
  extends Controller with HasDatabaseConfigProvider[JdbcProfile] {

  def list = TODO

  def create = TODO

  def update = TODO

  def remove(id: Long) = TODO
}
