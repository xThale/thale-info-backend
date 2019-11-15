package thale.info.filter

import info.thale.http4k.auth.filter.exception.AuthenticationException
import info.thale.http4k.auth.filter.exception.AuthorizationException
import mu.KotlinLogging
import org.http4k.core.Filter
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.LensFailure
import thale.info.exception.BaseProblem
import thale.info.exception.Problem
import thale.info.exception.validation.LensProblem
import thale.info.lens.Lenses

/**
 * Catches all exceptions and parses them into a [Problem] response
 */
object ProblemFilter {

    private val log = KotlinLogging.logger {}

    operator fun invoke(): Filter = Filter { next ->
        {
            try {

                next(it) // If no exception is thrown, pass through the request

            } catch (e: AuthenticationException) { // Response for exception thrown in components of this project

                log.debug { "Authentication exception caught in ProblemFilter: ${e.message}" }
                val problem = BaseProblem(title = "Authentication exception occurred", details = e.message ?: "").toProblem()
                Response(Status.UNAUTHORIZED).with(Lenses.problem of problem)

            } catch (e: AuthorizationException) { // Response for exception thrown in components of this project

                log.debug { "Authorization exception caught in ProblemFilter: ${e.message}" }
                val problem = BaseProblem(title = "Authorization exception occurred", details = e.message ?: "").toProblem()
                Response(Status.FORBIDDEN).with(Lenses.problem of problem)

            } catch (e: BaseProblem) { // Response for exception thrown in components of this project

                log.debug { "BaseProblem caught in ProblemFilter: ${e.message}" }
                Response(e.status).with(Lenses.problem of e.toProblem())

            } catch (e: LensFailure) { // Thrown if the request/response could not be parsed

                log.debug { "BaseProblem caught in ProblemFilter: ${e.message}" }
                Response(Status.BAD_REQUEST).with(Lenses.problem of LensProblem(e.message ?: "Lens error occurred").toProblem())

            } catch (e: Exception) { // Response for every other exception

                log.debug { "BaseProblem caught in ProblemFilter: ${e.message}" }
                val problem = BaseProblem(title = "Unexpected error occurred", details = e.message ?: "").toProblem()
                Response(Status.INTERNAL_SERVER_ERROR).with(Lenses.problem of problem)

            }
        }
    }
}