package thale.info.route

import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.RequestContextLens
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import thale.info.api.model.LoginMethodType
import thale.info.api.model.LoginResponse
import thale.info.api.model.UserResponse
import thale.info.auth.AuthTokenService
import thale.info.config.ConfigProvider
import thale.info.dataaccess.User
import thale.info.lens.Lenses
import thale.info.mapper.toTokenInfoModel
import thale.info.mapper.toUserModel

fun authRoute(userContext :  RequestContextLens<User>, config: ConfigProvider): RoutingHttpHandler = "/auth" bind routes(


    "/user" bind Method.GET to { request ->
        val user = userContext(request)
        val response = UserResponse().user(user.toUserModel())
        Response(Status.OK).with(Lenses.userResponse of response)
    },



    /**
     * Called if a user wants to login and exchange an authorization token for an id token and refresh token
     * Returns [Status.BAD_REQUEST] if the login method [LoginMethodType] is not supported
     * Returns [Status.UNAUTHORIZED] if the code is invalid
     * Returns [Status.FORBIDDEN] if the user is not registered (not found in the database)
     * Returns [Status.OK] if the token was successfully exchanged and parsed into a [TokenExchangeResponse]
     * @return [TokenExchangeResponse] on success
     */
    "/login" bind Method.POST to { request ->
        val loginRequest = Lenses.loginRequest(request) // Get request body object

        val userTokenPair = AuthTokenService.getAuthTokenService(loginRequest.loginMethod, config) // Get corresponding authenticator
            .exchangeAuthorizationCode(loginRequest.token) // Exchange code for tokens

        val response = LoginResponse()
            .token(userTokenPair.second.toTokenInfoModel())
            .user(userTokenPair.first.toUserModel())

        Response(Status.OK).with(Lenses.loginResponse of response) // Send tokens as response
    }
)