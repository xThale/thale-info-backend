package thale.info

import info.thale.http4k.auth.filter.authentication.AuthenticationFilter
import info.thale.http4k.auth.filter.authorization.AuthorizationFilter
import info.thale.http4k.auth.filter.config.AuthFilterConfiguration
import info.thale.http4k.auth.filter.endpoint.EndpointSignature
import mu.KotlinLogging
import org.http4k.core.RequestContexts
import org.http4k.core.then
import org.http4k.filter.CorsPolicy
import org.http4k.filter.ServerFilters
import org.http4k.lens.RequestContextKey
import org.http4k.routing.routes
import org.http4k.server.Netty
import org.http4k.server.asServer
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.inject
import org.koin.core.logger.Level
import org.koin.core.logger.Logger
import org.koin.core.logger.MESSAGE
import org.koin.dsl.binds
import org.koin.dsl.module
import thale.info.api.model.LoginMethodType
import thale.info.auth.AuthTokenService
import thale.info.auth.GoogleAuthTokenService
import thale.info.config.ConfigProvider
import thale.info.config.DefaultConfigProvider
import thale.info.config.HttpConfigProvider
import thale.info.dataaccess.User
import thale.info.database.DatabaseService
import thale.info.database.MongoDatabaseService
import thale.info.exception.authentication.UserNotRegisteredProblem
import thale.info.filter.ProblemFilter
import thale.info.route.authRoute
import thale.info.route.todoRoute
import thale.info.route.userRoute
import thale.info.service.CardService
import thale.info.service.UserService

class Main : KoinComponent {

    private val config by inject<ConfigProvider>()

    private val todoService by inject<CardService>()
    private val securityService by inject<UserService>()

    private fun run() {

        val port = config.http.port
        log.info("Starting application on port $port")

        val contexts = RequestContexts()
        val userContext = RequestContextKey.required<User>(contexts)

        val api = routes( // define routes for http4k
            todoRoute(todoService),
            userRoute(userContext, securityService),
            authRoute(userContext, config)
        )

        val authFilterConfiguration = AuthFilterConfiguration<User, String>()

        authFilterConfiguration.issuerTokenAuthenticators.registerGoogleTokenAuthenticator(config.auth.google.clientId) { token ->
                val mail = AuthTokenService.getAuthTokenService(LoginMethodType.GOOGLE, config)
                    .getMailFromToken(token)
                securityService.getUserByEmail(mail) ?: throw UserNotRegisteredProblem("User for mail $mail was not found.")
            }

        authFilterConfiguration.endpointSecurityConfig.registerUnsecuredEndpoint("/auth/login")


        ServerFilters.InitialiseRequestContext(contexts)
            .then(ServerFilters.Cors(CorsPolicy.UnsafeGlobalPermissive))
            .then(ServerFilters.CatchAll())
            .then(ProblemFilter()) // catch exceptions and transform then into a universal response
            .then(AuthenticationFilter(authFilterConfiguration).authenticate(userContext)) // handling authentication of the request
            .then(AuthorizationFilter(authFilterConfiguration).authorize(userContext)) // handling authorization of the request per endpoint
            .then(api).asServer(Netty(port)).start() // start server on port


        Runtime.getRuntime().addShutdownHook(object : Thread() {
            override fun start() {
                log.info("Stop server since JVM is shutting down")
            }
        })
    }

    companion object {

        private val log = KotlinLogging.logger {}

        // define module for koin
        private val modules = module {
            single<ConfigProvider> { DefaultConfigProvider() } binds (arrayOf(
                HttpConfigProvider::class
            ))

            single<AuthTokenService> { GoogleAuthTokenService(get()) }

            single<DatabaseService> { MongoDatabaseService() }
            single { CardService(get())}
            single { UserService(get()) }
        }

        @JvmStatic
        fun main(args: Array<String>) {
            startKoin {
                // use slf4j logger instead of writing of log messages
                // into stdout/stderr
                logger(object : Logger() {
                    override fun log(level: Level, msg: MESSAGE) {
                        when (level) {
                            Level.DEBUG -> log.debug(msg)
                            Level.INFO -> log.info(msg)
                            Level.ERROR -> log.error(msg)
                        }
                    }
                })
                modules(modules)
            }

            Main().run()
        }
    }

}