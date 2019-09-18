package thale.info.database

import com.mongodb.client.MongoDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.getCollection
import org.litote.kmongo.save
import thale.info.dataaccess.Todo
import java.util.*

/**
 * Database service for the [Todo] entity
 */
class TodoDatabaseService(db: MongoDatabase) {

    private val col = db.getCollection<Todo>()

    fun createTodo(todo: Todo) = todo.apply { col.save(this) }

    fun getAllTodo() = col.find().toSet()

    fun findTodoByUUID(uuid: UUID) = col.findOne(Todo::uuid eq uuid)
}