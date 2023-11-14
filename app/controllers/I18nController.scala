package controllers

import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.mvc.*

import javax.inject.{Inject, Singleton}

@Singleton
class I18nController @Inject()(cc: ControllerComponents, messagesApi: MessagesApi)
  extends AbstractController(cc) with I18nSupport {

  def getMessage(key: String): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val localizedMessage = Messages(key)(request2Messages)
    Ok(localizedMessage)
  }
}