package thale.info.exception.authentication

/**
 * Thrown if an exception occurred, while trying to refresh the tokens with the refresh token.
 */
class TokenRefreshProblem(message: String?)
    : AuthenticationProblem("Token could not be refreshed. Message: ${message ?: "unknown reason"}")