package thale.info.exception.authentication

import org.http4k.core.Status
import thale.info.exception.BaseProblem
import thale.info.exception.UserNotFoundProblem

/**
 * Thrown if a user was not found in the database, similar to [UserNotFoundProblem], however with status [Status.FORBIDDEN]
 */
open class UserNotRegisteredProblem(details: String)
    : BaseProblem(Status.FORBIDDEN, "User seems to not be registered for this application", details)