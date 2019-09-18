package thale.info.exception

import org.http4k.core.Status
import java.util.*

/**
 * Thrown if the user could not be found for an [UUID]
 */
class UnknownUserProblem(uuid: UUID)
    : BaseProblem(Status.NOT_FOUND, "User not found", "User with id $uuid not found")