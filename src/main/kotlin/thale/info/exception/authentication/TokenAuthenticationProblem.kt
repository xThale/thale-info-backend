package thale.info.exception.authentication

/**
 * Thrown if an exception occurred, while trying to authenticate the id token.
 * Most likely the token is expired.
 */
class TokenAuthenticationProblem(message: String?)
    : AuthenticationProblem("Bearer token could not be authenticated. Maybe the token is invalid or expired. Message: ${message ?: "unknown reason"}")