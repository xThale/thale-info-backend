package thale.info.api.security.authorization

import mu.KotlinLogging
import thale.info.api.security.EndpointSignature
import thale.info.api.security.getRequiredRolesForEndpoint
import thale.info.api.security.isSecuredEndpoint
import thale.info.dataaccess.User

class DefaultAuthorizationService : AuthorizationService {

    private val log = KotlinLogging.logger {}

    override fun authorized(user: User, endpointSignature: EndpointSignature) : Boolean {

        if (!isSecuredEndpoint(endpointSignature)) return true

        when(val requiredRoles = getRequiredRoles(endpointSignature)) {
            null -> {
                log.debug { "Endpoint not found in the role map. No roles required for the endpoint $endpointSignature" }
                return true
            }
            else -> {
                // See if the user has at one role that is required for the endpoint
                return when(hasAtLeastOneRole(requiredRoles, user)) {
                    true -> {
                        log.debug { "User permitted for the endpoint $endpointSignature" }
                        true
                    }
                    false -> {
                        log.debug { "User not permitted for the endpoint $endpointSignature" }
                        false
                    }
                }
            }
        }
    }

    private fun hasAtLeastOneRole(requiredRoles : List<String> , user: User) : Boolean {
        requiredRoles.forEach { requiredRole ->
            if (user.roles.contains(requiredRole)) return true
        }
        return false
    }

    private fun getRequiredRoles(endpointSignature: EndpointSignature) : List<String>? {
        log.debug { "Searching for matching endpoint $endpointSignature in the role map" }
        return getRequiredRolesForEndpoint(endpointSignature)
    }

}