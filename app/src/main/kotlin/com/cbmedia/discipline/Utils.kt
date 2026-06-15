package com.cbmedia.discipline

import androidx.compose.ui.graphics.Color
import com.cbmedia.discipline.model.Game
import com.cbmedia.discipline.model.GameSummary
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

val IceBlue = Color(0xFFB3E5FC)
val IceBlueDark = Color(0xFF4FC3F7)
val IceText = Color(0xFF00344D)

fun LocalDate.toUKFormat(): String = this.format(DateTimeFormatter.ofPattern("d MMM yyyy"))

fun isGameFrozen(freezeEndsOn: LocalDate?) = freezeEndsOn?.let { LocalDate.now().isBefore(it) } == true

fun Game.toSummary(): GameSummary =
    GameSummary(
        id = id,
        name = name,
        remainingDays = state.remainingDays,
        deckCount = state.deck.size,
        discardCount = state.discardPile.size,
        lastDrawnCard = state.lastDrawnCard,
        freezeEndsOn = state.freezeEndsOn,
        totalDays = daysActive()
    )

fun Game.daysActive(): Int =
    ChronoUnit.DAYS.between(
        createdDate,
        endedDate ?: LocalDate.now()
    ).toInt()
