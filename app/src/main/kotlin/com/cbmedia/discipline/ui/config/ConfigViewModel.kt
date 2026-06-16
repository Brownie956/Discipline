package com.cbmedia.discipline.ui.config

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbmedia.discipline.GameEngine
import com.cbmedia.discipline.data.GameRepository
import com.cbmedia.discipline.model.CardType
import com.cbmedia.discipline.model.Game
import com.cbmedia.discipline.model.GameMode
import com.cbmedia.discipline.timerToMinutes
import com.cbmedia.discipline.toUKFormat
import com.cbmedia.discipline.ui.components.TimerUnit
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime

class ConfigViewModel(
    private val repository: GameRepository
) : ViewModel() {

    var cardCounts by mutableStateOf(
        CardType.entries.associateWith { 0 }
    )
        private set

    var gameName by mutableStateOf("")
        private set

    var baseTimerAmount by mutableStateOf("30")
        private set

    var baseTimerUnit by mutableStateOf(TimerUnit.DAYS)
        private set

    var drawIntervalAmount by mutableStateOf("1")
        private set

    var drawIntervalUnit by mutableStateOf(TimerUnit.DAYS)
        private set

    var useCardCountAsBaseDays by mutableStateOf(false)
        private set

    val totalSelectedCards: Int
        get() = cardCounts.values.sum()

    val resolvedBaseDays: Int
        get() = if (useCardCountAsBaseDays) {
            totalSelectedCards
        } else {
            baseTimerAmount.toIntOrNull()?.coerceAtLeast(1) ?: 1
        }

    fun updateGameName(name: String) {
        gameName = name
    }

    fun updateBaseTimerAmount(value: String) {
        baseTimerAmount = value.filter { it.isDigit() }
    }

    fun updateBaseTimerUnit(unit: TimerUnit) {
        baseTimerUnit = unit
    }

    fun updateDrawIntervalAmount(value: String) {
        drawIntervalAmount = value.filter { it.isDigit() }
    }

    fun updateDrawIntervalUnit(unit: TimerUnit) {
        drawIntervalUnit = unit
    }

    fun updateUseCardCountAsBaseDays(checked: Boolean) {
        useCardCountAsBaseDays = checked
    }

    fun increment(cardType: CardType) {
        cardCounts = cardCounts.toMutableMap().also {
            it[cardType] = (it[cardType] ?: 0) + 1
        }
    }

    fun decrement(cardType: CardType) {
        cardCounts = cardCounts.toMutableMap().also {
            it[cardType] = maxOf(0, (it[cardType] ?: 0) - 1)
        }
    }

    fun setPresetCardCount(mode: GameMode) {
        cardCounts = mode.cardCounts
    }

    @OptIn(ExperimentalTime::class)
    fun createGame(
        onGameCreated: (Long) -> Unit
    ) {
        viewModelScope.launch {
            val baseTimerMinutes = if (useCardCountAsBaseDays) {
                totalSelectedCards.toLong() * TimerUnit.DAYS.minutesMultiplier
            } else {
                timerToMinutes(baseTimerAmount, baseTimerUnit)
            }

            val drawIntervalMinutes = timerToMinutes(drawIntervalAmount, drawIntervalUnit)

            val game = Game(
                id = 0,
                name = gameName.ifBlank {
                    Clock.System.now().toUKFormat()
                },
                state = GameEngine.startGame(
                    baseTimerMinutes = baseTimerMinutes,
                    cardCounts = cardCounts
                ),
                createdDate = Clock.System.now(),
                baseTimer = baseTimerMinutes.minutes,
                drawInterval = drawIntervalMinutes.minutes,
            )

            val gameId = repository.createGame(game)
            onGameCreated(gameId)
        }
    }
}
