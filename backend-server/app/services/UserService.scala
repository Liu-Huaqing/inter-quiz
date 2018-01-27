package services

import javax.inject.Inject

import dao.UserDao
import forms.UserForm
import models.User
import play.api.{Configuration, Logger}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


/**
 * @author fybai.
 * @since 2018-01-16
 *
 */


class UserService @Inject()(userDao: UserDao, configuration: Configuration) {

  private val logger = Logger(getClass)

  def index(): Future[Seq[User]] = {
    userDao.index().map { users =>
      users :+ User.owner
    }
  }

  def find(userId: Int): Future[Option[User]] = {
    if (userId == 0) {
      Future.successful(Option(User.owner))
    } else {
      userDao.find(userId)
    }
  }

  def create(userForm: UserForm): Future[User] = {
    logger.info(s"Received create request ${userForm.userName}")
    if (userForm.userName == configuration.get[String]("admin.name")) {
      Future.successful(User.owner)
    } else {
      val newUser = User.newUser(userForm.userName)
      userDao.create(newUser).map {
        case i if i > 0 => newUser
        case _ => throw new Exception("创建新用户失败")
      }
    }
  }
}
