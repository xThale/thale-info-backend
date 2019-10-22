package thale.info.database

import com.mongodb.client.MongoDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.getCollection
import org.litote.kmongo.save
import thale.info.dataaccess.Card
import java.util.*

/**
 * Database service for the [Card] entity
 */
class CardDatabaseService(db: MongoDatabase) {

    private val col = db.getCollection<Card>()

    fun createCard(card: Card) = card.apply { col.save(this) }

    fun getAllCards() = col.find().toSet()

    fun findCardByUUID(uuid: UUID) = col.findOne(Card::uuid eq uuid)
}