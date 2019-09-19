package thale.info.config

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

interface HttpConfigProvider {
    val http: HttpConfig
}

class HttpConfig (
    val port : Int
)

interface AuthConfigProvider {
    val auth: AuthConfig
}

class AuthConfig (
    val google: GoogleConfig
)

class GoogleConfig (
    val clientId: String,
    val clientSecret: String
)


/**
 * Holds the configuration for the project
 */
interface ConfigProvider : HttpConfigProvider, AuthConfigProvider

class DefaultConfigProvider : ConfigProvider {
    override val http : HttpConfig by lazy {
        HttpConfig (
            port = conf.getInt("http.port")
        )
    }

    override val auth: AuthConfig by lazy {
        AuthConfig (
            GoogleConfig(
                conf.getString("auth.google.clientId"),
                conf.getString("auth.google.clientSecret")
            )
        )
    }

    // ensures the configuration is loaded once
    private companion object Loader {
        private val conf: Config = ConfigFactory.load()
    }
}