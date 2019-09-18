package thale.info.service

import thale.info.api.model.TodoFindResponse
import thale.info.api.model.TodoRequest
import thale.info.dataaccess.Todo
import thale.info.database.DatabaseService
import thale.info.mapper.toCreateResponse
import thale.info.mapper.toDto
import thale.info.mapper.toResponse
import java.util.*

class TodoService (private val db: DatabaseService) {

    fun createTodo(todo: TodoRequest) = db.todo.createTodo(todo.toDto()).toCreateResponse()

    fun getAllTodo(): TodoFindResponse = db.todo.getAllTodo().map(Todo::toResponse).let { TodoFindResponse().todos(it) }

    fun findTodoByUUID(uuid: UUID) = db.todo.findTodoByUUID(uuid)?.toResponse()

}