package thale.info.validation

import thale.info.dataaccess.User
import thale.info.exception.authorization.UnauthorizedProblem
import thale.info.api.security.Roles
import java.util.*

/**
 * Validates business logic for the user endpoint
 */
class UserEndpointValidator {

    /**
     * Validates request for GET ROLES endpoint.
     * Throw [UnauthorizedProblem], if the user requests roles of a different user without the [Roles.ADMIN] permission
     */
    fun validateGetRoles(uuid: UUID, user: User) {
        when {
            uuid != user.uuid && !user.roles.contains(Roles.ADMIN.name)
                -> throw UnauthorizedProblem("You lack permission to see other users roles")
        }
    }

}