package thale.info.exception

import org.http4k.core.Status
import thale.info.exception.BaseProblem
import java.util.*

/**
 * Thrown if the user could not be found in the database for an [UUID] or email
 */
class UserNotFoundProblem(uuid: UUID? = null, email: String? = null)
    : BaseProblem(Status.NOT_FOUND, "User not found",
    when {
        uuid != null -> "User with id $uuid not found"
        email != null -> "User with email $email not found"
        else -> ""
    }
)