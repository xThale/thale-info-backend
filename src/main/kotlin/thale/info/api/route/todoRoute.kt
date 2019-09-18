package thale.info.api.route

import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import thale.info.api.lens.Lenses
import thale.info.service.TodoService
import thale.info.util.extractUUID

fun todoRoute(todoService: TodoService): RoutingHttpHandler = "/todo" bind routes(
    "/" bind Method.POST to { request ->
        val response = Lenses.todoRequestLens(request).let { todoService.createTodo(it) }
        Response(Status.CREATED).with(Lenses.todoCreateResponseLens of response)

    },
    "/" bind Method.GET to {
        val response = todoService.getAllTodo()
        Response(Status.OK).with(Lenses.todoFindResponseLens of response)
    },
    "/{uuid}" bind Method.GET to { request ->
        val uuid = extractUUID(request)
        todoService.findTodoByUUID(uuid)?.let { Response(Status.OK).with(Lenses.todoResponseLens of it) } ?: Response(Status.NOT_FOUND)
    }
)
