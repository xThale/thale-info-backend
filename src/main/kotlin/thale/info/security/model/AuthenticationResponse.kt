package thale.info.security.model

/**
 * The successful response to a bearer token authentication
 */
interface AuthenticationResponse {

    /**
     * Parser the response into an [AuthenticatedUser] in order to find the corresponding user in the database
     * or create a new one based on the data in the response
     */
    fun toAuthenticatedUser() : AuthenticatedUser
}