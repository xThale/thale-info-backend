package thale.info.api.filter

import com.auth0.jwt.JWT
import mu.KotlinLogging
import org.http4k.core.Filter
import org.http4k.core.Status
import org.http4k.core.then
import org.http4k.filter.ServerFilters
import org.http4k.lens.RequestContextLens
import thale.info.api.security.EndpointSignature
import thale.info.api.security.authentication.TokenAuthenticator
import thale.info.api.security.authentication.model.AuthenticatedUser
import thale.info.api.security.isSecuredEndpoint
import thale.info.config.ConfigProvider
import thale.info.dataaccess.User
import thale.info.exception.BaseProblem
import thale.info.service.UserService

/**
 * This filter authenticates the bearer token in the request against an [TokenAuthenticator],
 * then finds the linked user to that token and puts this user in the context of the request.
 */
object AuthenticationFilter {

    private val log = KotlinLogging.logger {}

    operator fun invoke(userContext: RequestContextLens<User>,
                        userService: UserService,
                        config: ConfigProvider) = Filter { next ->
        { request ->
            when (isSecuredEndpoint(EndpointSignature(request))) { // Check if the endpoint is secured based on its signature
                true -> next(request) // This endpoint is unsecured, let the request through
                false -> {
                    // Else try to authenticate the request
                    ServerFilters.BearerAuth.invoke(userContext) { token ->
                        val jwt = JWT.decode(token) // Parse bearer token to jwt token
                        // Get the authenticator based on the issuer of the token
                        val tokenAuthenticator = TokenAuthenticator.getTokenAuthenticator(jwt.issuer)
                        val authUser = tokenAuthenticator.authenticate(jwt, config) // Authenticate token
                        findUser(authUser.email, userService) // If successful, find user for token and return it
                    }.then(next)(request)
                }
            }
        }
    }

    /**
     * Find the user based on [AuthenticatedUser.email] in the database
     */
    private fun findUser(mail: String, userService: UserService) : User {
        log.info { "Searching for $mail in the database" }
        return userService.getUserByEmail(mail) ?: throw BaseProblem(
            status = Status.INTERNAL_SERVER_ERROR,
            title = "Cannot find user for mail $mail",
            details = "User was not found for mail $mail even though the token was issued by us. This should not happen."
        )
    }

}