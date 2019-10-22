package thale.info.dataaccess

import thale.info.util.createUUID
import java.util.*

/**
 * The database entity holding a cards data
 */
data class Card(
    val _id: String? = null,
    val uuid: UUID = createUUID(),
    val front: String = "",
    val back: String = "",
    val leech: Boolean = false
)