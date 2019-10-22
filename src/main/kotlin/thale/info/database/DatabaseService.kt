package thale.info.database

/**
 * Service for accessing the database
 */
interface DatabaseService {
    val card : CardDatabaseService
    val user : UserDatabaseService
}