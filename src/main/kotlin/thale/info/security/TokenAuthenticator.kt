package thale.info.security

import thale.info.security.model.AuthenticatedUser

/**
 * Authenticates a bearer token
 */
interface TokenAuthenticator {

    /**
     * Authenticates the bearer token and returns an [AuthenticatedUser]
     */
    fun authenticate(token: String) : AuthenticatedUser
}