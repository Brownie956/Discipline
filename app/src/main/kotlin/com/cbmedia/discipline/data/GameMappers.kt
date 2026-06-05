package com.cbmedia.discipline.data

import com.cbmedia.discipline.data.local.GameEntity
import com.cbmedia.discipline.model.CardType
import com.cbmedia.discipline.model.Game
import com.cbmedia.discipline.model.GameState
import com.cbmedia.discipline.model.GameStatus
import java.time.LocalDate

fun Game.toEntity(): GameEntity {
    return GameEntity(
        id = id,
        name = name,
        remainingDays = state.remainingDays,
        deck = state.deck.joinToString(",") { it.name },
        discardPile = state.discardPile.joinToString(",") { it.name },
        lastDrawnCard = state.lastDrawnCard?.name,
        lastDrawDate = state.lastDrawDate?.toEpochDay(),
        freezeEndsOn = state.freezeEndsOn?.toEpochDay(),
        createdDate = createdDate.toEpochDay(),
        status = status.name,
        endedDate = endedDate?.toEpochDay()
    )
}

fun GameEntity.toGame(): Game {
    return Game(
        id = id,
        name = name,
        state = GameState(
            remainingDays = remainingDays,
            deck = deck.toCardList(),
            discardPile = discardPile.toCardList(),
            lastDrawnCard = lastDrawnCard?.let(CardType::valueOf),
            lastDrawDate = lastDrawDate?.let(LocalDate::ofEpochDay),
            freezeEndsOn = freezeEndsOn?.let(LocalDate::ofEpochDay)
        ),
        createdDate = LocalDate.ofEpochDay(createdDate),
        status = GameStatus.valueOf(status),
        endedDate = endedDate?.let(LocalDate::ofEpochDay)
    )
}

private fun String.toCardList(): List<CardType> {
    if (isBlank()) return emptyList()

    return split(",").map(CardType::valueOf)
}
