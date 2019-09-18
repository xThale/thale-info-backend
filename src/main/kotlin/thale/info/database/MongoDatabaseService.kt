package thale.info.database

import org.litote.kmongo.KMongo


class MongoDatabaseService : DatabaseService {

    companion object {
        private val client = KMongo.createClient() // Create client for mongodb
        private val database = client.getDatabase("thale") // connect to the database
    }

    override val todo = TodoDatabaseService(database)
    override val user = UserDatabaseService(database)

}