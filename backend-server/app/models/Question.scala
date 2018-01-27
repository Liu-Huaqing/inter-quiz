package models

import models.Question.QuestionOption
import play.api.libs.json.{JsValue, Json}

/**
 * @author fybai.
 * @since 2018-01-20
 *
 */


case class Question(questionId: Int, title: String, options: Array[QuestionOption], answerIndex: Int) {

  def select(optionIndex: Int, user: User): Boolean = {
    if (optionIndex >= 0 && optionIndex < options.length) {
      options(optionIndex).select(user)
    }

    user.lastQuestionId = user.questionId
    user.questionId = questionId
    user.selectedIndex = optionIndex

    if (answerIndex != optionIndex) {
      user.isAlive = false
      false
    } else {
      true
    }
  }

  def getQuestionResponse: QuestionResponseContent = {
    QuestionResponseContent(questionId, title, options.map(_.option))
  }

  def getAnswerResponse: AnswerResponseContent = {
    AnswerResponseContent(title, options.map(_.option), options.map(_.stat), answerIndex)
  }
}

object Question {

  case class QuestionOption(option: String) {

    var stat: Int = 0
    var users: Seq[User] = Seq.empty

    def select(user: User): Unit = {
      stat += 1
      users = users :+ user
    }

    override def toString: String = {
      s"option: $option, stat: $stat"
    }
  }

  val empty: Question = Question(-1, "", Array.empty, -1)

  @volatile var questions: Array[Seq[Question]] = loadFromJsonFile()

  private def loadFromJsonFile(fileName: String = "questions.json"): Array[Seq[Question]] = {
    val stream = getClass.getClassLoader.getResourceAsStream(fileName)
    val jsonQuestions: JsValue = try {
      Json.parse(stream)
    }
    finally {
      stream.close()
    }

    try {
      (jsonQuestions \\ "items").map { question =>
        question.asOpt[List[Map[String, JsValue]]] match {
          case Some(questions) =>
            questions.zipWithIndex.map {
              case (question, index) =>
                val title = question("title").as[String]
                val options = question("selects").as[Array[String]].map(option => QuestionOption(option))
                val answerIndex = question("resultIndex").as[String].toInt
                Question(index, title, options, answerIndex)
            }
          case None =>
            throw new Exception("json 错误")
        }
      }.toArray
    } catch {
      case e: Exception =>
        throw new Exception("json 错误", e)
    }
  }

  def getQuestion(questionCategoryId: Int, questionId: Int): Option[Question] = {
    if (questionCategoryId < questions.length) {
      questions(questionCategoryId).find(_.questionId == questionId)
    } else {
      None
    }
  }

  def reset(): Unit = {
    questions = loadFromJsonFile()
  }
}