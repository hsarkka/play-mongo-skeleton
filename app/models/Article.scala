package models

import play.api.data._
import play.api.data.Forms._
import play.api.libs.json._
import play.api.libs.functional.syntax._

/** Article model */
case class Article(
  title: String,
  description: String)

/** Companion object for article model */
object Article {

  // Reader from JSON to model object
  implicit val articleRead: Reads[Article] = (
    (JsPath \ "title").read[String] and
    (JsPath \ "description").read[String])(Article.apply _)

  // Writer from model object to JSON
  implicit val articleWrites: Writes[Article] = (
    (JsPath \ "title").write[String] and
    (JsPath \ "description").write[String])(unlift(Article.unapply))

  // Form object
  val articleForm: Form[Article] = Form(
    mapping(
      "title" -> nonEmptyText,
      "description" -> nonEmptyText)(Article.apply)(Article.unapply))

}
