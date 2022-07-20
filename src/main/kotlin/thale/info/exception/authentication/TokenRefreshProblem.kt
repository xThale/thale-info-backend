package thale.info.exception.authentication

import org.http4k.core.Status
import thale.info.exception.BaseProblem

/**
 * Thrown if an exception occurred, while trying to refresh the tokens with the refresh token.
 */
class TokenRefreshProblem(message: String?)
    : BaseProblem(Status.UNAUTHORIZED, "Authorization failed", "Token could not be refreshed. Message: ${message ?: "unknown reason"}")