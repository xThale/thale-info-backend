package thale.info.exception

import org.http4k.core.Status
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

/**
 * The base class for problems. ALl exceptions should extend this class
 */
open class BaseProblem(val status: Status = Status.INTERNAL_SERVER_ERROR,
                       override val message: String = "",
                       private val details: String = "",
                       private val timestamp: OffsetDateTime = OffsetDateTime.now()) : Exception(message) {

    fun toProblem() = Problem(status.toString(), message, details, timestamp.format(DateTimeFormatter.ISO_DATE_TIME))
}