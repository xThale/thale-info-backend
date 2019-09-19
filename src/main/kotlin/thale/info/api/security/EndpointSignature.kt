package thale.info.api.security

import org.http4k.core.Method
import org.http4k.core.Request

/**
 * Builds the endpoint signature for an request
 */
data class EndpointSignature(val method: Method, val path: String) {

    constructor(request: Request) : this(request.method, request.uri.path)

    val signature = "${method.name} $path"
}