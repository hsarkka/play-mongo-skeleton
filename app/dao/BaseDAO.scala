package dao

import play.api.Play.current
import play.modules.reactivemongo.ReactiveMongoPlugin

trait BaseDAO {

  /** Returns the default database (as specified in "application.conf"). */
  lazy val db = ReactiveMongoPlugin.db

}
