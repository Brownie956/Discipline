package com.cbmedia.discipline.model

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

data class GameState @OptIn(ExperimentalTime::class) constructor(
    val remainingMinutes: Long,
    val deck: List<CardType>,
    val discardPile: List<CardType>,
    val lastDrawnCard: CardType? = null,
    val lastDrawTime: Instant? = null,
    val freezeEndsAt: Instant? = null
)
