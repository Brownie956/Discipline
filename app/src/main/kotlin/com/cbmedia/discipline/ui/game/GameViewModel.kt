package com.cbmedia.discipline.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbmedia.discipline.GameEngine
import com.cbmedia.discipline.data.GameRepository
import com.cbmedia.discipline.model.Game
import com.cbmedia.discipline.model.GameState
import com.cbmedia.discipline.model.GameStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class GameViewModel(
    private val repository: GameRepository
) : ViewModel() {

    private val _gameId = MutableStateFlow<Long?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val game: StateFlow<Game?> =
        _gameId
            .filterNotNull()
            .flatMapLatest { gameId ->
                repository.observeGame(gameId)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )

    val gameState: GameState?
        get() = game.value?.state

    fun loadGame(gameId: Long) {
        _gameId.value = gameId
    }

    @OptIn(ExperimentalTime::class)
    fun drawCard() {
        val currentGame = game.value ?: return

        viewModelScope.launch {
            val result = GameEngine.drawCard(currentGame)
            val updatedState = result.newState

            val updatedGame = if (updatedState.remainingMinutes <= 0) {
                currentGame.copy(
                    state = updatedState.copy(remainingMinutes = 0),
                    status = GameStatus.COMPLETED,
                    endedDate = Clock.System.now()
                )
            } else {
                currentGame.copy(
                    state = updatedState
                )
            }

            repository.updateGame(updatedGame)
        }
    }

    @OptIn(ExperimentalTime::class)
    fun endGameEarly() {
        val currentGame = game.value ?: return

        viewModelScope.launch {
            repository.updateGame(
                currentGame.copy(
                    status = GameStatus.ABANDONED,
                    endedDate = Clock.System.now()
                )
            )
        }
    }
}
