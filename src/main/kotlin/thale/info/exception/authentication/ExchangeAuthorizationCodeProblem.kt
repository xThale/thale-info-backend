package thale.info.exception.authentication

import org.http4k.core.Status
import thale.info.exception.BaseProblem

/**
 * Thrown if a exception occurs, while exchanging a authorization code for an id token, access token and refresh token.
 */
class ExchangeAuthorizationCodeProblem(message: String?)
    : BaseProblem(Status.UNAUTHORIZED, "Authorization failed", "Token could not be refreshed. Message: ${message ?: "unknown reason"}")