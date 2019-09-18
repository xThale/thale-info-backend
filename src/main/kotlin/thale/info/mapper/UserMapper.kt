package thale.info.mapper

import thale.info.api.model.UserResponse
import thale.info.dataaccess.User

fun User.toResponse() : UserResponse {
    return UserResponse()
        .uuid(this.uuid)
        .avatar(this.avatar)
        .name(this.name)
        .email(this.email)
        .roles(this.roles)
}