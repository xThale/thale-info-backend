package thale.info.util

import org.http4k.core.Request
import org.http4k.routing.path
import thale.info.exception.BaseProblem
import java.util.*

fun extractUUID(request: Request) : UUID {
    return parseUUID(extractFromPath(request, "uuid"))
}

fun extractFromPath(request: Request, name: String): String {
    return request.path(name) ?: throw BaseProblem(title = "uuid parameter is missing")
}