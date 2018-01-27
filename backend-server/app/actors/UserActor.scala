package actors

import javax.inject.Inject

import akka.actor._
import models._
import play.api.Logger

import scala.concurrent.duration._

/**
 * @author fybai.
 * @since 2018-01-18
 *
 */


class UserActor @Inject()(out: ActorRef, user: User, room: Room) extends Actor {

  import context.dispatcher

  private val logger = Logger(getClass)

  // send the last status of this room to all user when user first connected.
  context.system.scheduler.scheduleOnce(0.second, out, Response(user, room.lastMessage))
  // send heartbeat to all user with online number
  context.system.scheduler.schedule(0.second, 3.seconds) {
    out ! Response(user, HeartbeatResponseContent(room.onlineNumber.get()))
  }

  override def preStart(): Unit = {
    room.addUser(user)
    if (room.isStarted) {
      room.progressBar.nowQuestion match {
        case Some(question) if question.questionId != user.questionId && question.questionId != user.lastQuestionId =>
          user.isAlive = false
        case _ =>
      }
    }
  }

  override def receive: Receive = {
    case s@StartGame(questionCategoryId, whenPeopleLessThenNGameOver) if isCommandFromRoomOwner(s) && !room.isStarted =>
      room.startGame(questionCategoryId, whenPeopleLessThenNGameOver)
      self ! NextQuestion
    case NextQuestion if isCommandFromRoomOwner(NextQuestion) =>
      nextQuestionOrEndGame()
    case EndGame if isCommandFromRoomOwner(EndGame) =>
      room.progressBar.nowQuestion.foreach(room.notSelectAnyAnswer)
      sendMessage(EndGameResponseContent(room.winners()))
    case ResetGame if isCommandFromRoomOwner(ResetGame) => resetGame()
    case g@GetAnswer(questionId) if isCommandFromRoomOwner(g) =>
      room.progressBar.nowQuestion.foreach(room.notSelectAnyAnswer)
      Question.getQuestion(room.questionCategoryId, questionId) match {
        case Some(question) =>
          sendMessage(question.getAnswerResponse)
        case None =>
          logger.info(s"Received GetAnswer from $user, but no question for $questionId, sending empty question.")
          sendMessage(Question.empty.getAnswerResponse)
      }
    case q@QuestionAck(questionId, optionIndex) =>
      Question.getQuestion(room.questionCategoryId, questionId) match {
        case Some(question) =>
          if (logger.isDebugEnabled) logger.debug(s"Received QuestionAck-$q from $user")
          if (question.select(optionIndex, user)) room.progressBar.allRightNumber.getAndIncrement()
        case None => logger.warn(s"Received QuestionAck from $user, but no question for $questionId")
      }
    case Heartbeat => self ! HeartbeatResponseContent(room.onlineNumber.get())
    case responseContent: ResponseContent =>
      responseContent match {
        case _: ResetGameResponseContent.type =>
          out ! Response(user, responseContent)
          self ! PoisonPill
        case _ => out ! Response(user, responseContent)
      }
    case unexpected =>
      logger.warn(s"Receive unexpected message-$unexpected from $user.")
  }

  private def nextQuestionOrEndGame(): Unit = {
    if (room.isGameOver) {
      sendMessage(EndGameResponseContent(room.winners()))
      logger.info(s"Game is over, because the left people is less then N")
    } else {
      room.progressBar.nextQuestion() match {
        case Some(question) =>
          sendMessage(question.getQuestionResponse)
        case None =>
          logger.info("No next question, and game is over.")
          sendMessage(EndGameResponseContent(room.winners()))
      }
    }
  }

  private def isCommandFromRoomOwner(cmd: RoomOwnerCommand): Boolean = {
    if (room.owner == user) {
      logger.info(s"Received ${cmd.getClass.getName} from $user who is room owner.")
      true
    } else false
  }

  private def sendMessage(responseContent: ResponseContent): Unit = {
    room.setLastMessage(responseContent)
    context.actorSelection("/user/*") ! responseContent
  }

  private def resetGame(): Unit = {
    room.reset()
    room.progressBar.reset()
    context.actorSelection("/user/*") ! ResetGameResponseContent
  }

  override def postStop(): Unit = {
    room.deleteUser(user)
    logger.info(s"$user disconnected.")
  }
}

object UserActor {

  def props(out: ActorRef, user: User, room: Room) =
    Props(new UserActor(out, user, room))
}
