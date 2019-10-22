package thale.info.exception

import org.http4k.core.Status
import thale.info.exception.BaseProblem

open class InsufficientRightsProblem(details: String)
    : BaseProblem(Status.FORBIDDEN, "User has not the required roles for this endpoint", details)