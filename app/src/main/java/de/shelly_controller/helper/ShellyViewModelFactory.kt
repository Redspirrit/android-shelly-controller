package de.shelly_controller.helper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.shelly_controller.model.ShellyRepository
import de.shelly_controller.viewModel.ShellyViewModel

class ShellyViewModelFactory(private val shellyRepository: ShellyRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShellyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ShellyViewModel(shellyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}