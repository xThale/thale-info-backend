package thale.info.api.filter

import mu.KotlinLogging
import org.http4k.core.Filter
import org.http4k.core.then
import org.http4k.filter.ServerFilters
import org.http4k.lens.RequestContextLens
import thale.info.dataaccess.User
import thale.info.database.DatabaseService
import thale.info.exception.AuthenticationProblem
import thale.info.mapper.toNewUser
import thale.info.security.TokenAuthenticator
import thale.info.security.model.AuthenticatedUser

/**
 * This filter authenticates the bearer token in the request against an [TokenAuthenticator],
 * then finds the linked user to that token (or creates a new user if not existing) and puts this user in the request
 * context
 */
object AuthenticationFilter {

    private val log = KotlinLogging.logger {}

    operator fun invoke(userContext: RequestContextLens<User>,
                        tokenAuthenticator: TokenAuthenticator,
                        databaseService: DatabaseService) = Filter { next ->
        {
            ServerFilters.BearerAuth.invoke(userContext) { token ->

                try {
                    findOrCreateOrUser(authenticate(token, tokenAuthenticator), databaseService)
                } catch (e: Exception) {
                    throw AuthenticationProblem(e.message ?: "Unknown reason")
                }

            }.then(next).invoke(it)
        }
    }

    /**
     * Authenticates the token against a [TokenAuthenticator]
     */
    private fun authenticate(token: String, tokenAuthenticator: TokenAuthenticator) : AuthenticatedUser {
        log.info("Authentication of token $token against google authenticator")
        return tokenAuthenticator.authenticate(token)
    }

    /**
     * Find the user based on [AuthenticatedUser] or creates a new user in the database via [DatabaseService]
     */
    private fun findOrCreateOrUser(authUser: AuthenticatedUser, databaseService: DatabaseService) : User {
        log.info { "Searching for ${authUser.email} in the database" }

        return when(val foundUser = databaseService.user.findUserByEmail(authUser.email)) {
            null -> {
                val newUser = authUser.toNewUser()
                log.info { "User not in the database. Create new user $newUser" }
                databaseService.user.createUser(newUser)
            }
            else -> {
                log.info { "Found user in the database" }
                foundUser
            }
        }
    }

}