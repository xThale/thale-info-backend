package thale.info.database

import org.litote.kmongo.KMongo

/**
 * Service for accessing the mongo database.
 * Contains all sub-services for data access
 */
class MongoDatabaseService : DatabaseService {

    companion object {
        private val client = KMongo.createClient() // Create client for mongodb
        private val database = client.getDatabase("thale") // connect to the database
    }

    override val card = CardDatabaseService(database)
    override val user = UserDatabaseService(database)

}