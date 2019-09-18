package thale.info.exception

import org.http4k.core.Status

/**
 * Thrown if user passed data has the wrong format, e. g. the uuid is not valid
 */
class FormatProblem(details: String)
    : BaseProblem(Status.BAD_REQUEST, "Format does not match", details)