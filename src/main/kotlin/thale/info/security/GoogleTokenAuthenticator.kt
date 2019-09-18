package thale.info.security

import mu.KotlinLogging
import org.http4k.client.JavaHttpClient
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import thale.info.api.lens.Lenses
import thale.info.security.model.AuthenticatedUser

/**
 * The [TokenAuthenticator] for a google bearer token
 */
class GoogleTokenAuthenticator : TokenAuthenticator {

    companion object {
        private val log = KotlinLogging.logger {}
        private val client: HttpHandler= JavaHttpClient()
        private const val uri = "https://www.googleapis.com/oauth2/v3/tokeninfo"
    }

    override fun authenticate(token: String) : AuthenticatedUser {
        try {
            val request = Request(Method.GET, uri).query("id_token", token)
            val response = client(request)
            when(response.status) {
                Status.OK -> return Lenses.googleAuthenticationResponse(response).toAuthenticatedUser() // Return authenticated user
                Status.BAD_REQUEST -> throw Exception("Token is either invalid or expired") // Token is invalid or expired
                else -> throw Exception("Response from google with error code ${response.status}") // Else throw exception
            }
        } catch (e : Exception) {
            log.error("Exception while requesting authentication from google for token $token. Message: ${e.message}")
            throw e
        }
    }

}