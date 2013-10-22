package models

import play.api.data._
import play.api.data.Forms._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import reactivemongo.bson.BSONObjectID
import play.modules.reactivemongo.json.BSONFormats._

/** Article model */
case class Article(
  id: BSONObjectID = BSONObjectID.generate,
  title: String = "",
  description: String = "",
  published: Boolean = false)

/** Companion object for article model */
object Article {

  // Reader from JSON to model object
  implicit val articleRead: Reads[Article] = (
    (JsPath \ "_id").read[BSONObjectID] and
    (JsPath \ "title").read[String].orElse(Reads.pure("")) and
    (JsPath \ "description").read[String].orElse(Reads.pure("")) and
    (JsPath \ "published").read[Boolean].orElse(Reads.pure(false)))(Article.apply _)

  // Writer from model object to JSON
  implicit val articleWrites: Writes[Article] = (
    (JsPath \ "_id").write[BSONObjectID] and
    (JsPath \ "title").write[String] and
    (JsPath \ "description").write[String] and
    (JsPath \ "published").write[Boolean])(unlift(Article.unapply))

  // Form object: Defines form fields and mappings between tuple and model object
  val articleForm: Form[Article] = Form(
    mapping(
      "id" -> text,
      "title" -> nonEmptyText,
      "description" -> nonEmptyText,
      "published" -> boolean) {
        (id, title, description, published) => Article(new BSONObjectID(id), title, description, published)
      } {
        article => Some((article.id.stringify, article.title, article.description, article.published))
      })
}
