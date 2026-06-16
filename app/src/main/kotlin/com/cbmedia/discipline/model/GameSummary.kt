package com.cbmedia.discipline.model

import java.time.LocalDate
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

data class GameSummary @OptIn(ExperimentalTime::class) constructor(
    val id: Long,
    val name: String,
    val remainingTime: Duration,
    val deckCount: Int,
    val discardCount: Int,
    val lastDrawnCard: CardType?,
    val freezeEndsAt: Instant?,
    val totalDays: Int
)
