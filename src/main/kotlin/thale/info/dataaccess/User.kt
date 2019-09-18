package thale.info.dataaccess

import thale.info.service.createUUID
import java.util.*

/**
 * The database entity holding user data
 */
data class User(
    val _id: String? = null,
    val uuid: UUID = createUUID(),
    val email: String,
    val avatar: String,
    val name: String,
    val roles: List<String> = emptyList()
)