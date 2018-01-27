package controllers

import javax.inject.{Inject, Singleton}

import actors.UserActor
import akka.actor.ActorSystem
import akka.stream.{Materializer, OverflowStrategy}
import models._
import play.api.libs.streams.ActorFlow
import play.api.mvc.WebSocket.MessageFlowTransformer
import play.api.mvc._
import services.UserService

import scala.concurrent.{ExecutionContext, Future}

/**
 * @author fybai.
 * @since 2018-01-16
 *
 */

@Singleton
class GameController @Inject()(cc: ControllerComponents,
                               userService: UserService)
                              (implicit system: ActorSystem, mat: Materializer, ec: ExecutionContext)
  extends AbstractController(cc) {

  private implicit val messageFlowTransformer: MessageFlowTransformer[Command, Response] =
    MessageFlowTransformer.jsonMessageFlowTransformer[Command, Response]

  def ws: WebSocket = WebSocket.acceptOrResult[Command, Response] {
    case rh =>
      getRoom(rh).map {
        case Some(user) if Room.onlyOneRoom.owner == user =>
          Right(ActorFlow.actorRef(out =>
            UserActor.props(out, user, Room.onlyOneRoom),
            overflowStrategy = OverflowStrategy.dropHead
          ))
        case Some(user) if Room.onlyOneRoom.owner != user =>
          Right(ActorFlow.actorRef(out =>
            UserActor.props(out, user, Room.onlyOneRoom),
            overflowStrategy = OverflowStrategy.dropHead
          ))
        case _ => Left(Redirect("/user"))
      }.recover {
        case _ => Left(Redirect("/user"))
      }
    case _ => Future.successful(Left(Redirect("/user")))
  }

  private def getRoom(request: RequestHeader): Future[Option[User]] = {
    request.cookies.get("userId").map(_.value) match {
      case Some(userId) => userService.find(userId.toInt)
      case _ => Future.failed(throw new Exception)
    }
  }
}
