package thale.info.auth

import com.auth0.jwt.interfaces.DecodedJWT
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import mu.KotlinLogging
import org.koin.core.KoinComponent
import org.koin.core.inject
import thale.info.config.GoogleConfig
import thale.info.dataaccess.User
import thale.info.exception.BaseProblem
import thale.info.exception.authentication.ExchangeAuthorizationCodeProblem
import thale.info.exception.authentication.TokenRefreshProblem
import thale.info.exception.authentication.UserNotRegisteredProblem
import thale.info.service.UserService
import java.time.OffsetDateTime
import java.time.ZoneOffset

/**
 * The [AuthTokenService] for a google bearer token
 */
class GoogleAuthTokenService(config: GoogleConfig) : AuthTokenService, KoinComponent {

    private val clientId = config.clientId
    private val clientSecret = config.clientSecret
    private val userService by inject<UserService>()

    companion object {
        private val log = KotlinLogging.logger {}
        private val jsonFactory : JsonFactory = JacksonFactory()
        private val httpTransport : HttpTransport = NetHttpTransport()
    }


    override fun getMailFromToken(token: DecodedJWT) : String {
        val idToken = GoogleIdToken.parse(jsonFactory, token.token)
        return idToken.payload.email
    }


    override fun exchangeAuthorizationCode(authorizationCode: String): Pair<User, TokenInfo> {
        try {

            val response = GoogleAuthorizationCodeTokenRequest(
                httpTransport,
                jsonFactory,
                clientId, clientSecret,
                authorizationCode, "http://localhost:3000").execute()

            val user = getUserFromGoogleIdToken(response)
            val token = TokenInfo(response.idToken, response.refreshToken, OffsetDateTime.now(ZoneOffset.UTC).plusSeconds(response.expiresInSeconds ?: 3600))
            return Pair(user, token)

        } catch (e : BaseProblem) {
            log.warn("Exception while exchanging google authorization code $authorizationCode for tokens. Message: ${e.message}")
            throw e
        } catch (e : Exception) {
            log.warn("Exception while exchanging google authorization code $authorizationCode for tokens. Message: ${e.message}")
            throw ExchangeAuthorizationCodeProblem(e.message)
        }
    }

    override fun refreshToken(refreshToken: String): Pair<User, TokenInfo> {
        try {

            val response = GoogleRefreshTokenRequest(
                httpTransport,
                jsonFactory, refreshToken,
                clientId, clientSecret).execute()

            val user = getUserFromGoogleIdToken(response)
            val token = TokenInfo(response.idToken, response.refreshToken, OffsetDateTime.now(ZoneOffset.UTC).plusSeconds(response.expiresInSeconds ?: 3600))
            return Pair(user, token)

        } catch (e : BaseProblem) {
            log.warn("Error while refreshing google token with refresh token $refreshToken. Message: ${e.message}")
            throw e
        } catch (e : Exception) {
            log.warn("Error while refreshing google token with refresh token $refreshToken. Message: ${e.message}")
            throw TokenRefreshProblem(e.message)
        }
    }

    private fun getUserFromGoogleIdToken(token: GoogleTokenResponse): User {
        val email = GoogleIdToken.parse(jsonFactory, token.idToken).payload.email
        // Check if user for mail exists. If not, then the user is not registered.
        return userService.getUserByEmail(email) ?: throw UserNotRegisteredProblem("User for mail $email was not found")
    }


}