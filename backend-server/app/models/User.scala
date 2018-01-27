package models

import java.util.concurrent.atomic.AtomicInteger

/**
 * @author fybai.
 * @since 2018-01-16
 *
 */


case class User(id: Int, name: String) {

  @volatile var isAlive: Boolean = true
  @volatile var questionId: Int = -1
  @volatile var lastQuestionId: Int = -1
  @volatile var selectedIndex: Int = -1

  override def toString: String = {
    s"id: $id, name: $name"
  }

  override def hashCode(): Int = {
    17 * id + 31 * name.hashCode
  }

  override def equals(obj: scala.Any): Boolean = obj match {
    case user: User if user.id == id && user.name == name => true
    case _ => false
  }
}


object User {

  private val userNumber = new AtomicInteger(1)

  val owner = User(0, "毅哥admin")

  def newUser(userName: String): User = {
    User(userNumber.getAndIncrement(), userName)
  }
}
