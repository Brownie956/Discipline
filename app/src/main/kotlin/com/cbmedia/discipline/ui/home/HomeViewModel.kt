package com.cbmedia.discipline.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbmedia.discipline.data.GameRepository
import com.cbmedia.discipline.model.Game
import com.cbmedia.discipline.model.GameSummary
import com.cbmedia.discipline.toSummary
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    private val repository: GameRepository
) : ViewModel() {

    val activeGameSummaries: StateFlow<List<GameSummary>> =
        repository.observeActiveGames()
            .map { games -> games.map(Game::toSummary) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val completedGameSummaries: StateFlow<List<GameSummary>> =
        repository.observeCompletedGames()
            .map { games -> games.map(Game::toSummary) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val abandonedGameSummaries: StateFlow<List<GameSummary>> =
        repository.observeAbandonedGames()
            .map { games -> games.map(Game::toSummary) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
}
