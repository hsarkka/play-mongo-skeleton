package dao

import play.api.Play.current
import play.modules.reactivemongo.ReactiveMongoPlugin

/**
 * Base trait for all of our Data Access Objects.
 */
trait BaseDAO {

  /** Returns the default database (as specified in "application.conf"). */
  lazy val db = ReactiveMongoPlugin.db

}
