package thale.info.api.route

import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.RequestContextLens
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import thale.info.api.lens.Lenses
import thale.info.api.model.LoginMethodType
import thale.info.config.ConfigProvider
import thale.info.dataaccess.User
import thale.info.mapper.toResponse
import thale.info.api.security.authentication.TokenAuthenticator
import thale.info.api.security.authentication.model.TokenExchangeResponse
import thale.info.service.UserService

fun authRoute(userContext :  RequestContextLens<User>, userService: UserService, config: ConfigProvider): RoutingHttpHandler = "/auth" bind routes(


    "/user" bind Method.GET to { request ->
        val user = userContext(request)
        Response(Status.OK).with(Lenses.userResponse of user.toResponse())
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
        val authenticator = TokenAuthenticator.getTokenAuthenticator(loginRequest.loginMethod) // Get corresponding authenticator
        val tokenExchangeResponse = authenticator.exchangeAuthorizationCode(loginRequest.token, userService, config) // Exchange code for tokens
        Response(Status.OK).with(Lenses.tokenExchangeResponse of tokenExchangeResponse) // Send tokens as response
    }
)