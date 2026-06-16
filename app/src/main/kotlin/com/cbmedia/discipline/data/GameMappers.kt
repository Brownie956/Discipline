package com.cbmedia.discipline.data

import com.cbmedia.discipline.data.local.GameEntity
import com.cbmedia.discipline.model.CardType
import com.cbmedia.discipline.model.Game
import com.cbmedia.discipline.model.GameState
import com.cbmedia.discipline.model.GameStatus
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
fun Game.toEntity(): GameEntity {
    return GameEntity(
        id = id,
        name = name,
        remainingMinutes = state.remainingMinutes,
        deck = state.deck.joinToString(",") { it.name },
        discardPile = state.discardPile.joinToString(",") { it.name },
        lastDrawnCard = state.lastDrawnCard?.name,
        lastDrawTime = state.lastDrawTime?.toEpochMilliseconds(),
        freezeEndsAt = state.freezeEndsAt?.toEpochMilliseconds(),
        createdDate = createdDate.toEpochMilliseconds(),
        status = status.name,
        endedDate = endedDate?.toEpochMilliseconds(),
        baseTimerMinutes = baseTimer.inWholeMinutes,
        drawIntervalMinutes = drawInterval.inWholeMinutes
    )
}

@OptIn(ExperimentalTime::class)
fun GameEntity.toGame(): Game {
    return Game(
        id = id,
        name = name,
        state = GameState(
            remainingMinutes = remainingMinutes,
            deck = deck.toCardList(),
            discardPile = discardPile.toCardList(),
            lastDrawnCard = lastDrawnCard?.let(CardType::valueOf),
            lastDrawTime = lastDrawTime?.let(Instant::fromEpochMilliseconds),
            freezeEndsAt = freezeEndsAt?.let(Instant::fromEpochMilliseconds)
        ),
        createdDate = Instant.fromEpochMilliseconds(createdDate),
        status = GameStatus.valueOf(status),
        endedDate = endedDate?.let(Instant::fromEpochMilliseconds),
        baseTimer = baseTimerMinutes.minutes,
        drawInterval = drawIntervalMinutes.minutes
    )
}

private fun String.toCardList(): List<CardType> {
    if (isBlank()) return emptyList()

    return split(",").map(CardType::valueOf)
}
