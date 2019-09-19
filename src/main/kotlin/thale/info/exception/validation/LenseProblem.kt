package thale.info.exception.validation

import org.http4k.core.Status
import org.http4k.lens.LensFailure
import thale.info.exception.BaseProblem

/**
 * Thrown if a [LensFailure] occurred, e. g. the request body could not be parsed
 */
class LensProblem(details: String) : BaseProblem(Status.BAD_REQUEST, "failed to parse object", details)