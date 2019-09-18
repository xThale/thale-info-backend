package thale.info.validation

import thale.info.dataaccess.User
import thale.info.exception.UnauthorizedProblem
import thale.info.security.role.Roles
import java.util.*

class UserPermissionValidator {

    fun validateGetRoles(uuid: UUID, user: User) {
        when {
            uuid != user.uuid && !user.roles.contains(Roles.ADMIN.name)
                -> throw UnauthorizedProblem("You lack permission to see other users roles")
        }
    }

    fun validatePostRoles(user: User) {
        when {
            !user.roles.contains(Roles.ADMIN.name)
            -> throw UnauthorizedProblem("You lack permission to see edit roles")
        }
    }


}