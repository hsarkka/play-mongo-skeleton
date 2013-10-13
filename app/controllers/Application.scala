package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import scala.concurrent.Future
import reactivemongo.api._
import play.modules.reactivemongo.MongoController
import play.modules.reactivemongo.json.collection.JSONCollection
import models.Article

object Application extends Controller with MongoController {

  /* 
   * Get a JSONCollection (a Collection implementation that is designed to 
   * work with JsObject, Reads and Writes.) Note that the `collection` 
   * is not a "val", but a "def". We do _not_ store the collection reference 
   * to avoid potential problems in development with Play hot-reloading. 
   */
  def articlesCollection: JSONCollection = db.collection[JSONCollection]("articles")

  def index = Action {
    Ok(views.html.index("Mongo app is running"))
  }

  def findById(id: String) = Action.async {
    val query = Json.obj("_id" -> Json.obj("$oid" -> id))
    val futureItem = articlesCollection.find(query).one[JsValue]

    futureItem.map {
      case Some(item) => Ok(views.html.article(Article(item)))
      case None => NotFound(Json.obj("message" -> "No such item"))
    }
  }

  def articleEdit = Action {
    Ok(views.html.articleEdit(Article.articleForm))
  }

  def articleSave = Action { implicit request =>
    val boundForm =  Article.articleForm.bindFromRequest

    Article.articleForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index("Error")),
      article => Ok)
  }

}
