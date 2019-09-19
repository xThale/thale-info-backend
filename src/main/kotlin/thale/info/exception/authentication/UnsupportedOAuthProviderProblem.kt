package thale.info.exception.authentication

import org.http4k.core.Status
import thale.info.exception.BaseProblem

/**
 * Thrown when the issuer field in the bearer token is not from a supported source.
 */
class UnsupportedOAuthProviderProblem(iss: String)
    : BaseProblem(Status.BAD_REQUEST, "The bearer token is not from a supported provider", "The provider $iss is not supported")