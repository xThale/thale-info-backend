package thale.info.mapper

import thale.info.dataaccess.User
import thale.info.security.model.AuthenticatedUser
import thale.info.security.role.Roles

fun AuthenticatedUser.toNewUser() : User {
    return User(email = this.email,
        avatar = this.avatar,
        name = this.name,
        roles = listOf(Roles.USER.name))
}