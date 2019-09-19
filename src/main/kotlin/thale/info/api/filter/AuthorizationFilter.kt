package thale.info.api.filter

import mu.KotlinLogging
import org.http4k.core.Filter
import org.http4k.lens.RequestContextLens
import thale.info.api.security.EndpointSignature
import thale.info.api.security.authorization.AuthorizationService
import thale.info.dataaccess.User
import thale.info.exception.authorization.UnauthorizedProblem

/**
 * Checks if the authenticated user has the required roles for an endpoint based on [EndpointRoleMap]
 */
object AuthorizationFilter {

    private val log = KotlinLogging.logger {}

    operator fun invoke(userContext :  RequestContextLens<User>, endpointAuthorization: AuthorizationService) = Filter { next ->
        { request ->
            val user = userContext(request) // Extract the user from the request
            val endpointSignature = EndpointSignature(request) // Build the key for this endpoint

            log.debug { "Check authorization for endpoint $endpointAuthorization" }
            when(endpointAuthorization.authorized(user, endpointSignature)) {
                true -> next(request)
                false -> throw UnauthorizedProblem("You lack the necessary role for the endpoint $endpointSignature")
            }
        }
    }
}