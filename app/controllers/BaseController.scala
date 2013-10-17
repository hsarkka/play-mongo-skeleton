package controllers

import scala.concurrent.ExecutionContext

/**
 * Base trait for our controllers. Defines some implicit things that are needed
 * for asynchronous request handling.
 */
trait BaseController {

  implicit def ec: ExecutionContext = ExecutionContext.Implicits.global

}
