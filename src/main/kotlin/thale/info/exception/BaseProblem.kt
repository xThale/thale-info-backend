package thale.info.exception

import org.http4k.core.Status
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

/**
 * The universal problem, which is returned to api calls as response to exceptions
 */
class Problem(val status: Int,
              val message: String,
              val details: String,
              val timestamp: String)

/**
 * The base class for problems. ALl application exceptions should extend this class.
 * When returning this class as response, call [toProblem]
 */
open class BaseProblem(val status: Status = Status.INTERNAL_SERVER_ERROR,
                       val title: String = "",
                       private val details: String = "",
                       private val timestamp: OffsetDateTime = OffsetDateTime.now(),
                       override val message: String = "$title: $details") : Exception(message) {

    fun toProblem() = Problem(status.code, title, details, timestamp.format(DateTimeFormatter.ISO_DATE_TIME))
}