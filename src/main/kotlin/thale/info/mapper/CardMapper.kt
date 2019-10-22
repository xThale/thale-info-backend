package thale.info.mapper

import thale.info.api.model.CardCreateResponse
import thale.info.api.model.CardRequest
import thale.info.api.model.CardResponse
import thale.info.dataaccess.Card

fun CardRequest.toCard() : Card {
    return Card(front = this.front, back = this.back, leech = this.isLeech)
}

fun Card.toCreateResponse() : CardCreateResponse {
    return CardCreateResponse().uuid(this.uuid)
}

fun Card.toResponse() : CardResponse {
    return CardResponse().uuid(this.uuid).front(this.front).back(this.back).leech(this.leech)
}