package thale.info.exception.validation

import org.http4k.core.Status
import thale.info.exception.BaseProblem

/**
 * Thrown if user passed data has the wrong format, e. g. the uuid is not valid
 */
class FormatValidationProblem(details: String)
    : BaseProblem(Status.BAD_REQUEST, "Passed data has an invalid format", details)