package thale.info.api.security.authentication

import com.auth0.jwt.interfaces.DecodedJWT
import com.google.api.client.googleapis.auth.oauth2.*
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import mu.KotlinLogging
import thale.info.config.ConfigProvider
import thale.info.exception.authentication.ExchangeAuthorizationCodeProblem
import thale.info.exception.authentication.TokenAuthenticationProblem
import thale.info.exception.authentication.TokenRefreshProblem
import thale.info.exception.authentication.UserNotRegisteredProblem
import thale.info.exception.validation.UserNotFoundProblem
import thale.info.api.security.authentication.model.AuthenticatedUser
import thale.info.api.security.authentication.model.TokenExchangeResponse
import thale.info.service.UserService
import java.time.OffsetDateTime
import java.time.ZoneOffset

/**
 * The [TokenAuthenticator] for a google bearer token
 */
class GoogleTokenAuthenticator : TokenAuthenticator {

    companion object {
        private val log = KotlinLogging.logger {}
        private val jsonFactory : JsonFactory = JacksonFactory()
        private val httpTransport : HttpTransport = NetHttpTransport()
    }

    override fun authenticate(token: DecodedJWT, config : ConfigProvider) : AuthenticatedUser {
        try {
            val idToken = GoogleIdToken.parse(jsonFactory, token.token) // Parse jwt to google id token

            if (!idToken.verifyAudience(listOf(config.auth.google.clientId))) {
                throw Exception("Token is not intended for this application") // The client id must be correct
            }

            if (!GoogleIdTokenVerifier(
                    httpTransport,
                    jsonFactory
                ).verify(idToken)) {
                throw Exception("Token is not valid for google authentication") // The token is not valid, e. g. it expired
            }

            return getAuthenticatedUserFromPayload(idToken.payload) // Get user info from token
        } catch (e : Exception) {
            log.warn("Exception while requesting authentication from google for token $token. Message: ${e.message}")
            throw TokenAuthenticationProblem(e.message)
        }
    }

    override fun exchangeAuthorizationCode(authorizationCode: String, userService: UserService, config: ConfigProvider): TokenExchangeResponse {
        try {

            val response = GoogleAuthorizationCodeTokenRequest(
                httpTransport,
                jsonFactory,
                config.auth.google.clientId, config.auth.google.clientSecret,
                authorizationCode, "http://localhost:3000").execute()

            val email = GoogleIdToken.parse(jsonFactory, response.idToken).payload.email

            try {
                userService.getUserByEmail(email) // Check if user for mail exists. If not, then the user is not registered.
            } catch (e: UserNotFoundProblem) {
                log.warn { "User for email $email not found in database. Denying access to login" }
                throw UserNotRegisteredProblem(e.title)
            }

            return TokenExchangeResponse(
                response.idToken,
                response.refreshToken,
                OffsetDateTime.now(ZoneOffset.UTC).plusSeconds(response.expiresInSeconds ?: 3600)
            )

        } catch (e : Exception) {
            log.warn("Exception while exchanging google authorization code $authorizationCode for tokens. Message: ${e.message}")
            throw ExchangeAuthorizationCodeProblem(e.message)
        }
    }

    override fun refreshToken(refreshToken: String, config: ConfigProvider): TokenExchangeResponse {
        try {

            val response = GoogleRefreshTokenRequest(
                httpTransport,
                jsonFactory, refreshToken,
                config.auth.google.clientId, config.auth.google.clientSecret).execute()

            return TokenExchangeResponse(
                response.idToken,
                response.refreshToken,
                OffsetDateTime.now(ZoneOffset.UTC).plusSeconds(response.expiresInSeconds ?: 3600)
            )

        } catch (e : Exception) {
            log.warn("Error while refreshing google token with refresh token $refreshToken. Message: ${e.message}")
            throw TokenRefreshProblem(e.message)
        }
    }

    private fun getAuthenticatedUserFromPayload(payload: GoogleIdToken.Payload) : AuthenticatedUser {
        try {
            val email = payload.email
            val name = payload["name"]!! as String
            val avatar = payload["picture"]!! as String
            return AuthenticatedUser(email, avatar, name)
        } catch (e: Exception) {
            log.error { "The google payload seems to be erroneous. Payload: $payload" }
            throw Exception("Failed to get user infos from the google token payload. Message: ${e.message}")
        }
    }


}