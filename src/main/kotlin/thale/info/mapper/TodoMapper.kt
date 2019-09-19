package thale.info.mapper

import thale.info.api.model.TodoCreateResponse
import thale.info.api.model.TodoRequest
import thale.info.api.model.TodoResponse
import thale.info.dataaccess.Todo

fun TodoRequest.toTodo() : Todo {
    return Todo(title = this.title, description = this.description, done = this.isDone)
}

fun Todo.toCreateResponse() : TodoCreateResponse {
    return TodoCreateResponse().uuid(this.uuid)
}

fun Todo.toResponse() : TodoResponse {
    return TodoResponse().uuid(this.uuid).title(this.title).description(this.description).done(this.done)
}