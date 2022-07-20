package thale.info.validation

import thale.info.auth.Roles
import thale.info.dataaccess.User
import thale.info.exception.InsufficientRightsProblem
import java.util.*

/**
 * Validates business logic for the user endpoint
 */
class UserEndpointRoleValidator {

    /**
     * Validates request for GET ROLES endpoint.
     * Throw [InsufficientRightsProblem], if the user requests roles of a different user without the [Roles.ADMIN] permission
     */
    fun validateRolesForEndpointGetRoles(uuid: UUID, user: User) {
        when {
            uuid != user.uuid && !user.roles.contains(Roles.ADMIN.name)
                -> throw InsufficientRightsProblem("You lack permission to see other users roles")
        }
    }

}