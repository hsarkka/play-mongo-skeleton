package models

case class Article(
  title: String,
  description: String)

object JsonFormats {
  import play.api.libs.json.Json
  import play.api.data._
  import play.api.data.Forms._

  implicit val articleFormat = Json.format[Article]

  val articleForm = Form(
    mapping(
      "title" -> text,
      "description" -> text)(Article.apply _)(Article.unapply _))
}
