package com.cbmedia.discipline

import androidx.compose.ui.graphics.Color
import com.cbmedia.discipline.model.Game
import com.cbmedia.discipline.model.GameSummary
import java.time.LocalDate
import java.time.format.DateTimeFormatter

val IceBlue = Color(0xFFB3E5FC)
val IceBlueDark = Color(0xFF4FC3F7)
val IceText = Color(0xFF00344D)

fun LocalDate.toUKFormat(): String = this.format(DateTimeFormatter.ofPattern("d MMM yyyy"))

fun Game.toSummary(): GameSummary =
    GameSummary(
        id = id,
        name = name,
        remainingDays = state.remainingDays,
        deckCount = state.deck.size,
        discardCount = state.discardPile.size,
        lastDrawnCard = state.lastDrawnCard,
        freezeEndsOn = state.freezeEndsOn
    )

fun Color.inverted(): Color {
    return Color(
        red = 1f - red,
        green = 1f - green,
        blue = 1f - blue,
        alpha = alpha
    )
}
