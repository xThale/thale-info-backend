package thale.info.exception

import org.http4k.core.Status

/**
 * Thrown if the user lacks the needed roles for an endpoint
 */
class UnauthorizedProblem(details: String)
    : BaseProblem(Status.FORBIDDEN, "Your permissions are not sufficient", details)