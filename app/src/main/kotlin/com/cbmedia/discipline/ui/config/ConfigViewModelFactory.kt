package com.cbmedia.discipline.ui.config

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cbmedia.discipline.data.GameRepository

class ConfigViewModelFactory(
    private val repository: GameRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ConfigViewModel(repository) as T
    }
}
