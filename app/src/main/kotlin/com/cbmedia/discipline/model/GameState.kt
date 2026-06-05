package com.cbmedia.discipline.model

import java.time.LocalDate

data class GameState(
    val remainingDays: Int,
    val deck: List<CardType>,
    val discardPile: List<CardType>,
    val lastDrawnCard: CardType? = null,
    val lastDrawDate: LocalDate? = null,
    val freezeEndsOn: LocalDate? = null
)
