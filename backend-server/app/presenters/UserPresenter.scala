package presenters

import models.User
import play.api.libs.json.{JsValue, Json, Writes}

/**
 * @author fybai.
 * @since 2018-01-18
 *
 */


case class UserPresenter(
                          id: Int,
                          name: String
                        ) {

}

object UserPresenter {

  implicit val userWrites = Json.format[UserPresenter]

  def apply(user: User): UserPresenter =
    new UserPresenter(user.id, user.name)
}
