package models

import java.util.concurrent.atomic.AtomicInteger

/**
 * @author fybai.
 * @since 2018-01-17
 *
 */


case class ProgressBar() {

  private var questions = Question.questions(0).zipWithIndex.iterator // 默认第一题库
  @volatile var nowQuestion: Option[Question] = None // 当前房间内正在进行的答题
  @volatile var allRightNumber: AtomicInteger = new AtomicInteger(0) // 当前这一题答对的人数

  def setQuestionCategory(questionCategoryId: Int): Unit = {
    if (questionCategoryId < Question.questions.length) {
      questions = Question.questions(questionCategoryId).zipWithIndex.iterator
    }
  }

  def nextQuestion(): Option[Question] = {
    if (questions.hasNext) {
      val (question, idx) = questions.next()
      this.nowQuestion = Some(question.copy(questionId = idx))
      this.allRightNumber = new AtomicInteger(0)
      this.nowQuestion
    } else None
  }

  def reset(): Unit = {
    Question.reset()
    questions = Question.questions(0).zipWithIndex.iterator
    this.allRightNumber = new AtomicInteger(0)
    nowQuestion = None
  }
}

