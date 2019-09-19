package thale.info.api.security.authentication

import com.auth0.jwt.interfaces.DecodedJWT
import thale.info.api.model.LoginMethodType
import thale.info.config.ConfigProvider
import thale.info.exception.authentication.UnsupportedOAuthProviderProblem
import thale.info.exception.authentication.UnsupportedLoginMethodProblem
import thale.info.api.security.authentication.model.AuthenticatedUser
import thale.info.api.security.authentication.model.TokenExchangeResponse
import thale.info.service.UserService

/**
 * Authenticates a bearer token
 */
interface TokenAuthenticator {

    companion object {
        /**
         * Creates a new token authenticator based on the login method type [LoginMethodType]
         */
        fun getTokenAuthenticator(type: LoginMethodType) : TokenAuthenticator {
            return when(type) {
                LoginMethodType.GOOGLE -> GoogleTokenAuthenticator()
                else -> throw UnsupportedLoginMethodProblem(type)
            }
        }
        /**
         * Creates a new token authenticator based on the issuer field in the jwt token
         */
        fun getTokenAuthenticator(issuer: String) : TokenAuthenticator {
            return when(issuer) {
                "accounts.google.com" -> GoogleTokenAuthenticator()
                else -> throw UnsupportedOAuthProviderProblem(issuer)
            }
        }
    }

    /**
     * Authenticates the [DecodedJWT] bearer token and returns an [AuthenticatedUser].
     * Checks if the issuer is correct and the token is still valid.
     * @return Authenticated User fetched from the database based on the token information
     */
    fun authenticate(token: DecodedJWT, config: ConfigProvider) : AuthenticatedUser


    /**
     * Exchanges an oauth2 authorization code for an id_token, access_token and refresh_token
     * @return [TokenExchangeResponse] containing the id token and refresh token
     */
    fun exchangeAuthorizationCode(authorizationCode: String, userService: UserService, config: ConfigProvider): TokenExchangeResponse


    /**
     * Refreshes the tokens via a refresh token and returns on success a [TokenExchangeResponse]
     * containing the refreshed id token, the old refresh token and the new expiration date
     */
    fun refreshToken(refreshToken: String, config: ConfigProvider): TokenExchangeResponse
}