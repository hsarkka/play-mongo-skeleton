package controllers

import scala.concurrent.Future

import dao.ArticleDAO
import models.Article
import play.api.mvc.Action
import play.api.mvc.Controller

/**
 * Main front-end controller.
 */
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
  def articleEditNew = Action {
    val filledForm = Article.articleForm.fill(Article())
    Ok(views.html.articleEdit(filledForm))
  }

  // HTML request: Get edit form filled with the given article
  def articleEdit(id: String) = Action.async {
    val futArticle = ArticleDAO.findById(id)

    futArticle.map {
      case Some(article) =>
        val filledForm = Article.articleForm.fill(article)
        Ok(views.html.articleEdit(filledForm))
      case None => NotFound(views.html.notFound())
    }
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
    // Bind request to form object
    val boundForm = Article.articleForm.bindFromRequest
    // Show form errors, or save if there were no errors
    boundForm.fold(
      formWithErrors => Future(BadRequest(views.html.articleEdit(formWithErrors))),
      article => saveArticle(article))
  }
  // Helper: Tries to save the given article. Returns a Result according to success/failure.
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
