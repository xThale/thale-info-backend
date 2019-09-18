package thale.info.api.filter

import mu.KotlinLogging
import org.http4k.core.Filter
import org.http4k.lens.RequestContextLens
import thale.info.dataaccess.User
import thale.info.exception.UnauthorizedProblem
import thale.info.security.role.EndpointRoleMap

/**
 * Checks if the authenticated user has the required roles for an endpoint based on [EndpointRoleMap]
 */
object AuthorizationFilter {

    private val log = KotlinLogging.logger {}

    operator fun invoke(userContext :  RequestContextLens<User>) = Filter { next ->
        { request ->
            val user = userContext(request) // Extract the user from the request
            val endpointKey = "${request.method.name} ${request.uri.path}" // Build the key for this endpoint

            log.debug { "Searching for matching endpoint $endpointKey in the role map" }
            val mapKey = EndpointRoleMap.keys.firstOrNull{ it.toRegex().matches(endpointKey) } // Returns the first key which matches the endpoint key based on regex

            when(val neededRoles = EndpointRoleMap[mapKey]) {
                null -> {
                    log.debug { "Not found in the role map. No roles required for this endpoint" }
                    next(request)
                }
                else -> {

                    // See if the user has at one role that is required for the endpoint
                    val permitted = neededRoles.firstOrNull { neededRole ->
                        user.roles.contains(neededRole)
                    }

                    permitted?.let {
                        log.info { "User permitted for endpoint due to role $it" }
                        next(request) // At least one role matched, so permit the user to access the endpoint
                    }?: run {
                        log.info { "User not permitted for endpoint $endpointKey" }
                        throw UnauthorizedProblem("You lack the necessary role for the endpoint $endpointKey")
                    }


                }
            }

        }
    }
}