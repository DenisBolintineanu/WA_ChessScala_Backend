package utils

import play.api.i18n.{I18nSupport, MessagesApi, Messages}
import play.api.http.HttpErrorHandler
import play.api.http.Status.{BAD_REQUEST, NOT_FOUND}
import play.api.mvc.Results._
import play.api.mvc._

import javax.inject.{Singleton, Inject}
import scala.concurrent._

@Singleton
class ErrorHandler @Inject()(val messagesApi: MessagesApi) extends HttpErrorHandler with I18nSupport {
  
  implicit val messages: Messages = messagesApi.preferred(Seq.empty)

  override def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {
    Future.successful(NotFound(views.html.error()))
  }

  override def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = {
    Future.successful(NotFound(views.html.error()))
  }
}
