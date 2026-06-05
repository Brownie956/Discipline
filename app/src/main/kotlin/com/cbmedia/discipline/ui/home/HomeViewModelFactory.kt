package com.cbmedia.discipline.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cbmedia.discipline.data.GameRepository

class HomeViewModelFactory(
    private val repository: GameRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(repository) as T
    }
}
