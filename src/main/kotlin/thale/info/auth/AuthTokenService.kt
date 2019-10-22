package thale.info.auth

import com.auth0.jwt.interfaces.DecodedJWT
import thale.info.api.model.LoginMethodType
import thale.info.config.ConfigProvider
import thale.info.dataaccess.User
import thale.info.exception.authentication.UnsupportedLoginMethodProblem
import thale.info.service.UserService

/**
 * Authenticates a bearer token
 */
interface AuthTokenService {

    companion object {
        /**
         * Creates a new token authenticator based on the login method type [LoginMethodType]
         */
        fun getAuthTokenService(type: LoginMethodType, config: ConfigProvider) : AuthTokenService {
            return when(type) {
                LoginMethodType.GOOGLE -> GoogleAuthTokenService(config.auth.google)
                else -> throw UnsupportedLoginMethodProblem(type)
            }
        }
    }

    /**
     * Exchanges an oauth2 authorization code for an id_token, access_token and refresh_token
     * @return [TokenExchangeResponse] containing the id token and refresh token
     */
    fun exchangeAuthorizationCode(authorizationCode: String): Pair<User, TokenInfo>


    /**
     * Refreshes the tokens via a refresh token and returns on success a [TokenExchangeResponse]
     * containing the refreshed id token, the old refresh token and the new expiration date
     */
    fun refreshToken(refreshToken: String): Pair<User, TokenInfo>



    fun getMailFromToken(token: DecodedJWT) : String
}