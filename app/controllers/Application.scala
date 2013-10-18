package controllers

import scala.concurrent.Future

import dao.ArticleDAO
import models.Article
import play.api.libs.json.Json
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.api.mvc.Action
import play.api.mvc.Controller

object Application extends Controller with BaseController {

  // HTML request: Get front page
  def index = Action {
    Ok(views.html.index("Mongo app is running"))
  }

  // HTML request: Get single article by ID
  def findById(id: String) = Action.async {
    val futArticle = ArticleDAO.findById(id)

    futArticle.map {
      case Some(article) => Ok(views.html.article(article))
      case None => NotFound(views.html.notFound())
    }
  }

  // HTML request: Get edit form
  def articleEdit = Action {
    Ok(views.html.articleEdit(Article.articleForm))
  }

  // HTML request: Get list of articles
  def articleList = Action.async {
    val futPublishedArticles = ArticleDAO.getArticles(true)
    val futUnpublishedArticles = ArticleDAO.getArticles(false)

    for {
      publishedArticles <- futPublishedArticles
      unpublishedArticles <- futUnpublishedArticles
    } yield Ok(views.html.articleList(publishedArticles, unpublishedArticles))

  }

  // Form POST request: Submit edit form
  def articleSubmit = Action.async { implicit request =>
    val boundForm = Article.articleForm.bindFromRequest
    boundForm.fold(
      form => Future(BadRequest("Failed")),
      article => saveArticle(article))
  }

  // Helper: Try to save the given article in DB
  private def saveArticle(article: Article) = {
    val futError = ArticleDAO.saveArticle(article)
    futError.map {
      lastError =>
        lastError.inError match {
          case false => Ok(views.html.index("Saved"))
          case true => Ok(views.html.index("Error"))
        }
    }
  }

}
