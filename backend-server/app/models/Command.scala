package models

import play.api.libs.json._

/**
 * @author fybai.
 * @since 2018-01-17
 *
 */


sealed trait Command

sealed trait RoomOwnerCommand extends Command

sealed trait RoomUserCommand extends Command

/**
 * 开始游戏信号
 *
 * @param questionCategoryId          题库编号
 * @param whenPeopleLessThenNGameOver 当小于等于多少人时游戏结束
 */
case class StartGame(questionCategoryId: Int, whenPeopleLessThenNGameOver: Int) extends RoomOwnerCommand

/**
 * 下一题信号
 */
case object NextQuestion extends RoomOwnerCommand

/**
 * 获得答案信号
 *
 * @param questionId 题目编号
 */
case class GetAnswer(questionId: Int) extends RoomOwnerCommand

/**
 * 结束游戏信号
 */
case object EndGame extends RoomOwnerCommand

/**
 * 重置游戏信号
 */
case object ResetGame extends RoomOwnerCommand

/**
 * 答题者回答问题的答案
 *
 * @param questionId  回答的问题的编号 id
 * @param answerIndex 答题者选择了哪个选项
 */
case class QuestionAck(questionId: Int, answerIndex: Int) extends RoomUserCommand

case class UnexpectedCommand(msg: String) extends Command

case object Heartbeat extends Command


object Command {

  implicit val commandFormat = new Reads[Command] {

    override def reads(json: JsValue): JsResult[Command] = {
      (json \ "command").asOpt[String] match {
        case Some("start") =>
          JsSuccess(StartGame((json \ "question_category").as[Int], (json \ "winners_count").as[Int]))
        case Some("next_question") => JsSuccess(NextQuestion)
        case Some("next_answer") => JsSuccess(GetAnswer((json \ "question_id").as[Int]))
        case Some("finish") => JsSuccess(EndGame)
        case Some("question_ack") =>
          JsSuccess(QuestionAck((json \ "question_id").as[Int], (json \ "selected_index").as[Int]))
        case Some("reset") => JsSuccess(ResetGame)
        case Some("heartbeat") => JsSuccess(Heartbeat)
        case Some(_) | None => JsSuccess(UnexpectedCommand(json.toString()))
      }
    }
  }
}




