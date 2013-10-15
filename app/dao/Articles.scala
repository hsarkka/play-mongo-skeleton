package dao

import scala.concurrent.Future

import models.Article
import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.modules.reactivemongo.ReactiveMongoPlugin
import play.modules.reactivemongo.json.collection.JSONCollection

object Articles extends BaseDAO {

  /* 
   * Get a JSONCollection (a Collection implementation that is designed to 
   * work with JsObject, Reads and Writes.) Note that the `collection` 
   * is not a "val", but a "def". We do _not_ store the collection reference 
   * to avoid potential problems in development with Play hot-reloading. 
   */
  def articlesCollection: JSONCollection = db.collection[JSONCollection]("articles")

  // Get single article by ID
  def findById(id: String): Future[Option[Article]] = {
    // DB query
    val query = Json.obj("_id" -> Json.obj("$oid" -> id))
    val jsValueOptFut = articlesCollection.find(query).one[JsValue]

    // Convert JSON object to model object
    jsValueOptFut.map {
      case Some(jsVal) => Some(Article(jsVal))
      case None => None
    }
  }

  def saveArticle(article: Article) = {

  }

}
