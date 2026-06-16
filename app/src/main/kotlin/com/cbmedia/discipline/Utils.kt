package com.cbmedia.discipline

import androidx.compose.ui.graphics.Color
import com.cbmedia.discipline.model.Game
import com.cbmedia.discipline.model.GameSummary
import com.cbmedia.discipline.ui.components.TimerUnit
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.time.toJavaInstant

val IceBlue = Color(0xFFB3E5FC)
val IceBlueDark = Color(0xFF4FC3F7)
val IceText = Color(0xFF00344D)

@OptIn(ExperimentalTime::class)
fun Instant.toUKFormat(): String = this.toJavaInstant().atZone(ZoneId.systemDefault()).format(
    DateTimeFormatter.ofPattern("d MMM yyyy"))

fun timerToMinutes(amountText: String, unit: TimerUnit): Long =
    (amountText.toLongOrNull() ?: 1L).coerceAtLeast(1L) * unit.minutesMultiplier

@OptIn(ExperimentalTime::class)
fun isGameFrozen(freezeEndsAt: Instant?) = freezeEndsAt?.let { Clock.System.now() < it } ?: false

@OptIn(ExperimentalTime::class)
fun Game.toSummary(): GameSummary =
    GameSummary(
        id = id,
        name = name,
        remainingTime = state.remainingMinutes.minutes,
        deckCount = state.deck.size,
        discardCount = state.discardPile.size,
        lastDrawnCard = state.lastDrawnCard,
        freezeEndsAt = state.freezeEndsAt,
        totalDays = daysActive()
    )

@OptIn(ExperimentalTime::class)
fun Game.daysActive(): Int {
    val endTime = endedDate ?: Clock.System.now()

    return (endTime.minus(createdDate)).inWholeDays.toInt()
}

fun Duration.toDisplayText(): String {
    val days = inWholeDays
    val hours = inWholeHours % 24
    val minutes = inWholeMinutes % 60

    return when {
        days > 0 -> "${days}d ${hours}h ${minutes}m"
        hours > 0 -> "${hours}h ${minutes}m"
        else -> "${minutes}m"
    }
}
