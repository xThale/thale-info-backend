package thale.info.service

import thale.info.api.model.CardFindResponse
import thale.info.api.model.CardRequest
import thale.info.dataaccess.Card
import thale.info.database.DatabaseService
import thale.info.mapper.toCard
import thale.info.mapper.toCreateResponse
import thale.info.mapper.toResponse
import java.util.UUID

class CardService (private val db: DatabaseService) {

    fun createTodo(card: CardRequest) = db.card.createCard(card.toCard()).toCreateResponse()

    fun getAllTodo(): CardFindResponse = db.card.getAllCards().map(Card::toResponse).let { CardFindResponse().cards(it) }

    fun findTodoByUUID(uuid: UUID) = db.card.findCardByUUID(uuid)?.toResponse()

}