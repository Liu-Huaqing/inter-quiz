package services.actions

import javax.inject.Inject

import models.User
import play.api.mvc._
import play.api.mvc.Results._
import services.UserService

import scala.concurrent.{ExecutionContext, Future}


/**
 * @author fybai.
 * @since 2018-01-18
 *
 */


case class UserRequest[A](user: User, request: Request[A]) extends WrappedRequest[A](request)


class UserAction @Inject()(val parser: BodyParsers.Default, userService: UserService)(implicit val executionContext: ExecutionContext)
  extends ActionBuilder[UserRequest, AnyContent] with ActionRefiner[Request, UserRequest] {

  override protected def refine[A](request: Request[A]): Future[Either[Result, UserRequest[A]]] = {
    request.cookies.get("userId").map(_.value) match {
      case Some(userId) =>
        userService.find(userId.toInt).map(
          _.map(u => UserRequest(u, request)).toRight(Unauthorized)
        )
      case _ => Future.successful(Left(Unauthorized))
    }
  }
}
