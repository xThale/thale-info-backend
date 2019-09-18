package thale.info.database

import com.mongodb.client.MongoDatabase
import org.litote.kmongo.*
import thale.info.dataaccess.User
import java.util.*

/**
 * Database service for the [User] entity
 */
class UserDatabaseService(db: MongoDatabase) {

    private val col = db.getCollection<User>()

    fun createUser(user: User) = user.apply { col.save(this) }

    fun updateRolesByEmail(email: String, roles: List<String>) : User? =
        col.findOneAndUpdate(User::email eq email, setValue(User::roles, roles))

    fun findUserByEmail(email: String) = col.findOne(User::email eq email)

    fun findUserByUUID(uuid: UUID) = col.findOne(User::uuid eq uuid)
}