package models

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

import scala.collection.JavaConverters._

/**
 * @param id          房间 id
 * @param name        房间名称
 * @param owner       房间拥有者
 * @param progressBar 房间进度条
 */
case class Room(id: Int, name: String, owner: User, progressBar: ProgressBar) {

  /**
   * the user in this room
   */
  private var users = new ConcurrentHashMap[Int, User]()

  /**
   * the number of people in this room
   */
  var onlineNumber: AtomicInteger = new AtomicInteger(0)

  @volatile private var started: Boolean = false
  @volatile var lastMessage: ResponseContent = WaitingResponseContent

  @volatile var questionCategoryId: Int = 0
  @volatile var whenPeopleLessThenNGameOver: Int = 1

  def startGame(questionCategory: Int, whenPeopleLessThenNGameOver: Int): Unit = {
    this.whenPeopleLessThenNGameOver = whenPeopleLessThenNGameOver
    this.questionCategoryId = questionCategory
    progressBar.setQuestionCategory(questionCategory)
    progressBar.allRightNumber.set(onlineNumber.get())
    started = true
  }

  def setLastMessage(lastMessage: ResponseContent): Unit = {
    this.lastMessage = lastMessage
  }

  def isStarted: Boolean = started

  /**
   * somebody get into this room
   */
  def addUser(user: User): User = {
    onlineNumber.getAndIncrement()
    users.putIfAbsent(user.id, user)
  }

  /**
   * somebody get out this room
   */
  def deleteUser(user: User): User = {
    onlineNumber.decrementAndGet()
    users.remove(user.id)
  }

  def reset(): Int = {
    lastMessage = WaitingResponseContent
    val deletedSize = users.size()
    users = new ConcurrentHashMap[Int, User]()
    started = false
    deletedSize
  }

  def notSelectAnyAnswer(question: Question): Unit = {
    users.asScala.values.foreach { user =>
      if (question.questionId != user.questionId) {
        question.select(-1, user)
      }
    }
  }

  /**
   * the last winners
   */
  def winners(): Seq[User] = {
    users.asScala.values.filter(_.isAlive).toSeq
  }

  def isGameOver: Boolean = {
    started && progressBar.allRightNumber.get() <= whenPeopleLessThenNGameOver
  }
}

object Room {

  val onlyOneRoom = Room(0, "", User.owner, ProgressBar())
}




