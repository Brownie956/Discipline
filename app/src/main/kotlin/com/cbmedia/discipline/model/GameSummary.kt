package com.cbmedia.discipline.model

import java.time.LocalDate

data class GameSummary(
    val id: Long,
    val name: String,
    val remainingDays: Int,
    val deckCount: Int,
    val discardCount: Int,
    val lastDrawnCard: CardType?,
    val freezeEndsOn: LocalDate?
)
