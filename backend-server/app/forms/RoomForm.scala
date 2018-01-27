package forms

import play.api.data.Form
import play.api.data.Forms.{mapping, _}

/**
 * @author fybai.
 * @since 2018-01-17
 *
 */


case class RoomForm(roomName: String) {

}


object RoomForm {
  val roomForm = Form(
    mapping(
      "name" -> nonEmptyText(maxLength = 200)
    )(RoomForm.apply)(RoomForm.unapply)
  )
}



