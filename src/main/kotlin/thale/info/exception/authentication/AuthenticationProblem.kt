package thale.info.exception.authentication

import org.http4k.core.Status
import thale.info.exception.BaseProblem

/**
 * Base authentication problem, thrown if there were exceptions in the authentication process
 */
open class AuthenticationProblem(details: String)
    : BaseProblem(Status.UNAUTHORIZED, "Authentication failed", details)