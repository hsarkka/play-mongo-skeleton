package controllers

import scala.concurrent.Future

import dao.ArticleDAO
import models.Article
import play.api.libs.json.Json
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.api.mvc.Action
import play.api.mvc.Controller
import play.modules.reactivemongo.MongoController

object Application extends Controller with MongoController {

  // HTML request: Get front page
  def index = Action {
    Ok(views.html.index("Mongo app is running"))
  }

  // REST request: get single article by ID
  def findById(id: String) = Action.async {
    val futArticle = ArticleDAO.findById(id)
    
    futArticle.map {
      case Some(article) => Ok(views.html.article(article))
      case None => NotFound(Json.obj("message" -> "No such item"))
    }
  }

  // HTML request: Get edit form
  def articleEdit = Action {
    Ok(views.html.articleEdit(Article.articleForm))
  }

  def articleSave = Action.async { implicit request =>
    val boundForm = Article.articleForm.bindFromRequest

    
    Future(Ok(views.html.index("Saved")))
  }

}
