package controllers

import scala.concurrent.Future

import dao.ArticleDAO
import models.Article
import play.api.libs.json.Json
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.api.mvc.Action
import play.api.mvc.Controller

object Rest extends Controller with BaseController {

  // REST/JSON request: Get single article by ID
  def findById(id: String) = Action.async {
    val futArticle = ArticleDAO.findById(id)

    futArticle.map {
      case Some(article) => Ok(Json.toJson(article))
      case None => NotFound(Json.obj("message" -> "No such item"))
    }
  }

}
