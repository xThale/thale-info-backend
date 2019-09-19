package thale.info
import mu.KotlinLogging
import org.http4k.core.RequestContexts
import org.http4k.core.then
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
import thale.info.api.filter.AuthenticationFilter
import thale.info.api.filter.AuthorizationFilter
import thale.info.api.filter.ProblemFilter
import thale.info.api.route.authRoute
import thale.info.api.route.todoRoute
import thale.info.api.route.userRoute
import thale.info.config.ConfigProvider
import thale.info.config.DefaultConfigProvider
import thale.info.config.HttpConfigProvider
import thale.info.dataaccess.User
import thale.info.database.DatabaseService
import thale.info.database.MongoDatabaseService
import thale.info.api.security.authentication.GoogleTokenAuthenticator
import thale.info.api.security.authentication.TokenAuthenticator
import thale.info.api.security.authorization.AuthorizationService
import thale.info.api.security.authorization.DefaultAuthorizationService
import thale.info.service.UserService
import thale.info.service.TodoService

class Main : KoinComponent {

    private val config by inject<ConfigProvider>()

    private val userService by inject<UserService>()
    private val todoService by inject<TodoService>()
    private val securityService by inject<UserService>()
    private val authorizationService by inject<AuthorizationService>()

    private fun run() {

        val port = config.http.port
        log.info("Starting application on port $port")

        val contexts = RequestContexts()
        val userContext = RequestContextKey.required<User>(contexts)

        val api = routes( // define routes for http4k
            todoRoute(todoService),
            userRoute(userContext, securityService),
            authRoute(userContext, userService, config)
        )

        ServerFilters.InitialiseRequestContext(contexts)
            .then(ServerFilters.CatchAll())
            .then(ProblemFilter()) // catch exceptions and transform then into a universal response
            .then(AuthenticationFilter(userContext, userService, config)) // handling authentication of the request
            .then(AuthorizationFilter(userContext, authorizationService)) // handling authorization of the request per endpoint
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

            single<AuthorizationService> { DefaultAuthorizationService() }
            single<TokenAuthenticator> { GoogleTokenAuthenticator() }

            single<DatabaseService> { MongoDatabaseService() }
            single { TodoService(get())}
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