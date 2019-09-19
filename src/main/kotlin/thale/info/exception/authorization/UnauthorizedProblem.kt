package thale.info.exception.authorization

import org.http4k.core.Status
import thale.info.exception.BaseProblem

/**
 * Thrown if the user lacks the needed roles for an endpoint
 */
class UnauthorizedProblem(details: String)
    : BaseProblem(Status.FORBIDDEN, "Your permissions are not sufficient", details)