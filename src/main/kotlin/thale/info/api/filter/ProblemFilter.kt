package thale.info.api.filter

import org.http4k.core.Filter
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.LensFailure
import thale.info.api.lens.Lenses
import thale.info.exception.BaseProblem
import thale.info.exception.LensProblem
import thale.info.exception.Problem

/**
 * Catches all exceptions and parses them into a universal [Problem] response
 */
object ProblemFilter {
    operator fun invoke(): Filter = Filter { next ->
        {
            try {
                next(it)
            } catch (e: BaseProblem) {
                Response(e.status).with(Lenses.problem of e.toProblem())
            } catch (e: LensFailure) {
                Response(Status.BAD_REQUEST).with(Lenses.problem of LensProblem(e.message?:"Lens error occurred").toProblem())
            } catch (e: Exception) {
                val problem = BaseProblem(message = "Unexpected error occurred", details = e.message ?: "").toProblem()
                Response(Status.INTERNAL_SERVER_ERROR).with(Lenses.problem of problem)
            }
        }
    }
}