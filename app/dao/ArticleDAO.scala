package dao

import scala.concurrent.Future

import models.Article
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.JsObject
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.api.Cursor

/**
 * Data Access Object singleton for articles.
 */
object ArticleDAO extends BaseDAO {

  /**
   * Returns the MongoDB collection used by this DAO.
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

  /**
   * Returns the articles that match the given criteria.
   */
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
    articlesCollection.save(json)
  }

}
