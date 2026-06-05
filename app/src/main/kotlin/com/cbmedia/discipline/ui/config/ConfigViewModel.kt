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
import kotlinx.coroutines.launch
import java.time.LocalDate

class ConfigViewModel(
    private val repository: GameRepository
) : ViewModel() {

    var cardCounts by mutableStateOf(
        CardType.entries.associateWith { 0 }
    )
        private set

    var gameName by mutableStateOf("")
        private set

    var baseDays by mutableStateOf("30")
        private set

    var useCardCountAsBaseDays by mutableStateOf(false)
        private set

    val totalSelectedCards: Int
        get() = cardCounts.values.sum()

    val resolvedBaseDays: Int
        get() = if (useCardCountAsBaseDays) {
            totalSelectedCards
        } else {
            baseDays.toIntOrNull()?.coerceAtLeast(1) ?: 1
        }

    fun updateGameName(name: String) {
        gameName = name
    }

    fun updateBaseDays(value: String) {
        baseDays = value.filter { it.isDigit() }
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

    fun createGame(
        onGameCreated: (Long) -> Unit
    ) {
        viewModelScope.launch {
            val game = Game(
                id = 0,
                name = gameName.ifBlank {
                    "Game ${LocalDate.now()}"
                },
                state = GameEngine.startGame(
                    baseDays = resolvedBaseDays,
                    cardCounts = cardCounts
                ),
                createdDate = LocalDate.now()
            )

            val gameId = repository.createGame(game)
            onGameCreated(gameId)
        }
    }
}
