package dao

import java.util.concurrent.ConcurrentHashMap
import javax.inject._

import models.{Room, User}

import scala.concurrent.Future

/**
 * @author fybai.
 * @since 2018-01-17
 *
 */

@Singleton
class RoomDao extends AbstractDao {

  import RoomDao._

  def find(roomId: Int): Future[Option[Room]] = {
    rooms.get(roomId) match {
      case room: Room => Future.successful(Some(room))
      case _ => Future.successful(None)
    }
  }

  def create(room: Room): Future[Int] = {
    rooms.putIfAbsent(room.id, room) match {
      case room: Room => Future.successful(0)
      case _ => Future.successful(1)
    }
  }

  def userJoinRoom(room: Room, user: User): Future[Boolean] = {
    user2Room.put(user, room)
    Future.successful(true)
  }

  def getRoomByUser(user: User): Future[Option[Room]] = {
    Future.successful(Option(user2Room.get(user)))
  }
}

object RoomDao {

  private val rooms: ConcurrentHashMap[Int, Room] = new ConcurrentHashMap[Int, Room]()

  private val user2Room: ConcurrentHashMap[User, Room] = new ConcurrentHashMap[User, Room]()
}


