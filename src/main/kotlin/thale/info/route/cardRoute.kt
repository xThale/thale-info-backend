package thale.info.route

import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import thale.info.lens.Lenses
import thale.info.service.CardService
import thale.info.util.extractUUID

fun todoRoute(cardService: CardService): RoutingHttpHandler = "/card" bind routes(
    "/" bind Method.POST to { request ->
        val response = Lenses.cardRequestLens(request).let { cardService.createTodo(it) }
        Response(Status.CREATED).with(Lenses.todoCreateResponseLens of response)

    },
    "/" bind Method.GET to {
        val response = cardService.getAllTodo()
        Response(Status.OK).with(Lenses.todoFindResponseLens of response)
    },
    "/{uuid}" bind Method.GET to { request ->
        val uuid = extractUUID(request)
        cardService.findTodoByUUID(uuid)?.let { Response(Status.OK).with(Lenses.todoResponseLens of it) } ?: Response(Status.NOT_FOUND)
    }
)
