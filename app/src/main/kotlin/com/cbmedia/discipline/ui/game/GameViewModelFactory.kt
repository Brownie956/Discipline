package com.cbmedia.discipline.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cbmedia.discipline.data.GameRepository

class GameViewModelFactory(
    private val repository: GameRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GameViewModel(repository) as T
    }
}
