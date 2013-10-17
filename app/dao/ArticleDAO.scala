package dao

import scala.concurrent.Future

import models.Article
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.modules.reactivemongo.json.collection.JSONCollection

import play.api._
import play.api.mvc._

// Reactive Mongo imports
import reactivemongo.api._

// Reactive Mongo plugin
import play.modules.reactivemongo._
import play.modules.reactivemongo.json.collection.JSONCollection

// Play Json imports
import play.api.libs.json._

import play.api.Play.current
object ArticleDAO extends BaseDAO {

  /* 
   * Get a JSONCollection (a Collection implementation that is designed to 
   * work with JsObject, Reads and Writes.) Note that the `collection` 
   * is not a "val", but a "def". We do _not_ store the collection reference 
   * to avoid potential problems in development with Play hot-reloading. 
   */
  def articlesCollection: JSONCollection = db.collection[JSONCollection]("articles")

  /**
   * Retrieves a single article by ID.
   */
  def findById(id: String): Future[Option[Article]] = {
    // DB query
    val query = Json.obj("_id" -> Json.obj("$oid" -> id))
    val jsValueOptFut = articlesCollection.find(query).one[JsValue]

    // Convert (possible) JSON object to model object
    jsValueOptFut.map {
      case Some(jsVal) => Some(jsVal.as[Article])
      case None => None
    }
  }

  def getArticles(published: Boolean): Future[List[Article]] = {
    // DB query
    val cursor: Cursor[JsObject] = articlesCollection.find(Json.obj("published" -> published)).cursor[JsObject]
    // Convert cursor to list
    val futJsonList: Future[List[JsObject]] = cursor.toList

    // Convert JSON objects to model objects
    futJsonList.map {
      jsonList =>
        jsonList.map {
          jsObj => jsObj.as[Article]
        }
    }
  }

  /**
   * Saves the given article in the DB.
   */
  def saveArticle(article: Article) = {
    val json = Json.toJson(article)
    articlesCollection.insert(json)
  }

}
