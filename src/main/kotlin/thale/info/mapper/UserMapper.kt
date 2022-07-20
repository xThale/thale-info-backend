package thale.info.mapper

import thale.info.api.model.UserModel
import thale.info.api.model.UserResponse
import thale.info.dataaccess.User

/**
 * Maps a [User] to a [UserResponse]
 */
fun User.toUserModel() = UserModel().uuid(uuid).email(email).avatar(avatar).name(name).roles(roles)