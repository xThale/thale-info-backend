package thale.info.exception.authentication

import org.http4k.core.Status
import thale.info.exception.BaseProblem

/**
 * Thrown if a user tries to login while not being registered (not being in the database)
 */
open class UserNotRegisteredProblem(details: String)
    : BaseProblem(Status.FORBIDDEN, "User seems to not be registered for this application", details)