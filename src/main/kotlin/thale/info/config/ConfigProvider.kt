package thale.info.config

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

interface HttpConfigProvider {
    val http: HttpConfig
}

class HttpConfig (
    val port : Int
)

/**
 * Holds the configuration for the project
 */
interface ConfigProvider : HttpConfigProvider

class DefaultConfigProvider : ConfigProvider {
    override val http : HttpConfig by lazy {
        HttpConfig (
            port = conf.getInt("http.port")
        )
    }

    // ensures the configuration is loaded once
    private companion object Loader {
        private val conf: Config = ConfigFactory.load()
    }
}