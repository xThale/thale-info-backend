package thale.info.exception

import org.http4k.core.Status
import org.http4k.lens.LensFailure

/**
 * Thrown if a [LensFailure] occurred, e. g. the request body could not be parsed
 */
class LensProblem(override val message: String) : BaseProblem(Status.BAD_REQUEST, message, "failed to parse object")