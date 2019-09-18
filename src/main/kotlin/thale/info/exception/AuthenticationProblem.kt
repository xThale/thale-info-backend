package thale.info.exception

import org.http4k.core.Status

/**
 * Thrown when the user could not be authenticated
 */
class AuthenticationProblem(details: String)
    : BaseProblem(Status.UNAUTHORIZED, "Authentication failed", details)