package models

import play.api.data._
import play.api.data.Forms._
import play.api.libs.json._

/** Article model */
case class Article(
  title: String,
  description: String)

/** Helper object for articles */
object Article {

  // Factory for creating objects from JSON
  def apply(json: JsValue): Article = {
    Article(
      title = (json \ "title").as[String],
      description = (json \ "description").asOpt[String].getOrElse(""))
  }

  // Form object
  val articleForm: Form[Article] = Form(
    mapping(
      "title" -> nonEmptyText,
      "description" -> nonEmptyText)(Article.apply)(Article.unapply))

}
