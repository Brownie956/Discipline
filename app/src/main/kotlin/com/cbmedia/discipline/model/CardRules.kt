package com.cbmedia.discipline.model

import java.time.temporal.ChronoUnit
import kotlin.random.Random

object CardRules {
    const val GREEN_DAY_SUBTRACT = 1
    const val RED_DAYS_ADDED = 3
    const val YELLOW_REDS_ADDED = 3
    const val FREEZE_DAYS_MIN = 2
    const val FREEZE_DAYS_MAX_EXCLUSIVE = 8

    fun randomFreezeDays(random: Random = Random.Default): Int =
        random.nextInt(FREEZE_DAYS_MIN, FREEZE_DAYS_MAX_EXCLUSIVE)
}

fun CardType.describeLastDraw(state: GameState): String = when (this) {
    CardType.GREEN -> {
        val prefix = if (CardRules.GREEN_DAY_SUBTRACT == 1) "day" else "days"
        "${CardRules.GREEN_DAY_SUBTRACT} $prefix subtracted from days remaining"
    }

    CardType.RED, CardType.STICKY -> {
        val prefix = if (CardRules.RED_DAYS_ADDED == 1) "day" else "days"
        "${CardRules.RED_DAYS_ADDED} $prefix added to days remaining"
    }
    CardType.YELLOW ->
        "${CardRules.YELLOW_REDS_ADDED} additional RED cards added into deck"

    CardType.RESET ->
        "Deck reset and shuffled (excluding the RESET card)"

    CardType.DOUBLE ->
        "Number of RED and YELLOW cards in deck doubled"

    CardType.FREEZE, CardType.ARCTIC -> {
        val days = state.lastFreezeDays()
        days?.let {
            val postfix = if (days == 1) "day" else "days"
            "Countdown and deck frozen for $days $postfix"
        } ?: "Countdown and deck frozen"
    }
}

private fun GameState.lastFreezeDays(): Int? {
    val end = freezeEndsOn ?: return null
    val start = lastDrawDate ?: return null
    return ChronoUnit.DAYS.between(start, end).toInt()
}
