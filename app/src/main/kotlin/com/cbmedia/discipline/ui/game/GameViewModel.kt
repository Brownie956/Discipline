package com.cbmedia.discipline.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbmedia.discipline.GameEngine
import com.cbmedia.discipline.data.GameRepository
import com.cbmedia.discipline.model.Game
import com.cbmedia.discipline.model.GameState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

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

    fun drawCard() {
        val currentGame = game.value ?: return

        viewModelScope.launch {
            val result = GameEngine.drawCard(currentGame.state)

            repository.updateGame(
                currentGame.copy(
                    state = result.newState
                )
            )
        }
    }

    fun endGameEarly() {
        val currentGame = game.value ?: return

        viewModelScope.launch {
            repository.updateGame(
                currentGame.copy(
                    endedDate = LocalDate.now()
                )
            )
        }
    }
}
