package thale.info.database

/**
 * Service for accessing the database
 */
interface DatabaseService {
    val todo : TodoDatabaseService
    val user : UserDatabaseService
}