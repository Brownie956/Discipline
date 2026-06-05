package com.cbmedia.discipline.model

data class DrawResult(
    val drawnCard: CardType,
    val freezeDays: Int? = null,
    val newState: GameState
)
