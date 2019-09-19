package thale.info.exception.authentication

/**
 * Thrown if a exception occurs, while exchanging a authorization code for an id token, access token and refresh token.
 */
class ExchangeAuthorizationCodeProblem(message: String?)
    : AuthenticationProblem("Authorization code could not be exchanged for id token. Message: ${message ?: "unknown reason"}")