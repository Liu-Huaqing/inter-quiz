package controllers

import javax.inject._

import akka.actor.ActorSystem
import forms.UserForm
import play.api.data.Form
import play.api.libs.json.{JsArray, Json}
import play.api.mvc.Results._
import play.api.mvc.{Action, AnyContent, Cookie, DefaultActionBuilder}
import presenters.UserPresenter
import services.UserService

import scala.concurrent.{ExecutionContext, Future}

/**
 * @author fybai.
 * @since 2018-01-16
 *
 */

@Singleton
class UserController @Inject()(userService: UserService, actorSystem: ActorSystem,
                               defaultActionBuilder: DefaultActionBuilder)
                              (implicit exec: ExecutionContext) {

  def create(): Action[AnyContent] = defaultActionBuilder.async { implicit request =>
    UserForm.userForm.bindFromRequest().fold(
      errors => {
        Future.successful(UnprocessableEntity(Json.toJson(convertFormError(errors))))
      },
      userForm => {
        userService.create(userForm).map { u =>
          Ok(Json.toJson(UserPresenter(u))).withCookies(Cookie("userId", u.id.toString, domain = Some("inter-quiz.top"))).bakeCookies()
        }
      }
    )
  }

  private def convertFormError[T](formWithErrors: Form[T]): Map[String, String] = {
    formWithErrors.errors.foldLeft(Map.empty[String, String])((map, error) => {
      if (error.key.isEmpty) {
        map ++ Map(error.message -> error.args.mkString(", "))
      } else {
        map ++ Map(error.key -> error.message)
      }
    })
  }

  def index() = defaultActionBuilder.async { implicit request =>
    userService.index().map { users =>
      Ok(JsArray(users.map(u => Json.toJson(UserPresenter(u)))))
    }
  }
}
