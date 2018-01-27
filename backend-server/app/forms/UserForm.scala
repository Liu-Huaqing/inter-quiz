package forms

import play.api.data.Form
import play.api.data.Forms.{mapping, nonEmptyText}

/**
 * @author fybai.
 * @since 2018-01-16
 *
 */


case class UserForm(userName: String) {

}

object UserForm {
  val userForm = Form(
    mapping(
      "name" -> nonEmptyText(maxLength = 200)
    )(UserForm.apply)(UserForm.unapply)
  )
}
