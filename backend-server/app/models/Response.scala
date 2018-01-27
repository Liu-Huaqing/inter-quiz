package models

import play.api.libs.json._

/**
 * @author fybai.
 * @since 2018-01-16
 *
 */

// @formatter:off
sealed trait ResponseContent
case object WaitingResponseContent extends ResponseContent
case class HeartbeatResponseContent(onlineNumber: Int) extends ResponseContent
case class QuestionResponseContent(questionId: Int, title: String, options: Seq[String]) extends ResponseContent
case class AnswerResponseContent(title: String, options: Seq[String], stats: Seq[Int], answerIndex: Int) extends ResponseContent
case class EndGameResponseContent(winners: Seq[User]) extends ResponseContent
case object ResetGameResponseContent extends ResponseContent
// @formatter:on

object ResponseContent {

  implicit val waitingResponseContentFormat = new Writes[WaitingResponseContent.type] {
    override def writes(o: WaitingResponseContent.type): JsValue = {
      Json.obj(
        "command" -> "waiting"
      )
    }
  }

  implicit val heartbeatResponseContentFormat = new Writes[HeartbeatResponseContent] {
    override def writes(o: HeartbeatResponseContent): JsValue = {
      Json.obj(
        "command" -> "heartbeat",
        "online" -> o.onlineNumber
      )
    }
  }

  implicit val questionResponseContentFormat = new Writes[QuestionResponseContent] {
    override def writes(o: QuestionResponseContent): JsValue = {
      Json.obj(
        "command" -> "question",
        "question_id" -> JsNumber(o.questionId),
        "title" -> JsString(o.title),
        "options" -> JsArray(o.options.map(JsString))
      )
    }
  }

  implicit val answerResponseContentFormat = new Writes[AnswerResponseContent] {
    override def writes(o: AnswerResponseContent): JsValue = {
      Json.obj(
        "command" -> "answer",
        "title" -> JsString(o.title),
        "options" -> JsArray(o.options.map(JsString)),
        "stats" -> JsArray(o.stats.map(JsNumber(_))),
        "answer_index" -> JsNumber(o.answerIndex))
    }
  }

  implicit val endGameResponseContentFormat = new Writes[EndGameResponseContent] {
    override def writes(o: EndGameResponseContent): JsObject = {
      Json.obj(
        "command" -> "finish",
        "winners" -> o.winners.map(o => o.name)
      )
    }
  }

  implicit val resetGameResponseContentFormat = new Writes[ResetGameResponseContent.type] {
    override def writes(o: ResetGameResponseContent.type) = {
      Json.obj(
        "command" -> "reset"
      )
    }
  }

  implicit val responseContentFormat = new Writes[ResponseContent] {
    override def writes(o: ResponseContent): JsValue = {
      o match {
        case q: QuestionResponseContent => Json.toJson(q)
        case a: AnswerResponseContent => Json.toJson(a)
        case w: WaitingResponseContent.type => Json.toJson(w)
        case h: HeartbeatResponseContent => Json.toJson(h)
        case e: EndGameResponseContent => Json.toJson(e)
        case r: ResetGameResponseContent.type => Json.toJson(r)
      }
    }
  }
}

case class Response(user: User, responseContent: ResponseContent)

object Response {

  implicit val responseFormat = new Writes[Response] {
    override def writes(o: Response): JsValue = {
      val result = Json.toJson(o.responseContent).asInstanceOf[JsObject] ++
        Json.obj(
          "alive" -> o.user.isAlive
        )

      o.responseContent match {
        case _: AnswerResponseContent => result ++ Json.obj("selected_index" -> o.user.selectedIndex)
        case _ => result
      }
    }
  }
}





