package dao

import java.util.concurrent.ConcurrentHashMap
import javax.inject.Singleton

import models.User

import scala.collection.JavaConverters._
import scala.concurrent.Future

/**
 * @author fybai.
 * @since 2018-01-16
 *
 */

@Singleton
class UserDao extends AbstractDao {

  import UserDao._

  def index(): Future[Seq[User]] = {
    Future.successful(users.values().asScala.toSeq)
  }

  def find(userId: Int): Future[Option[User]] = {
    users.get(userId) match {
      case user: User => Future.successful(Some(user))
      case _ => Future.successful(None)
    }
  }

  def create(user: User): Future[Int] = {
    users.putIfAbsent(user.id, user) match {
      case user: User => Future.successful(0)
      case _ => Future.successful(1)
    }
  }
}

object UserDao {

  private val users: ConcurrentHashMap[Int, User] = new ConcurrentHashMap[Int, User]()
}

