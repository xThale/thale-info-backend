package thale.info.dataaccess

import thale.info.service.createUUID
import java.util.*

/**
 * The database entity holding todos data
 */
data class Todo(
    val _id: String? = null,
    val uuid: UUID = createUUID(),
    val title: String,
    val description: String = "",
    val done: Boolean = false
)