package de.shelly_controller.viewModel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import de.shelly_controller.model.ColorMix
import de.shelly_controller.model.Shelly
import de.shelly_controller.model.ShellyAction
import de.shelly_controller.model.ShellyRepository
import javax.inject.Inject

@HiltViewModel
class ShellyViewModel @Inject constructor(private val repo: ShellyRepository): ViewModel() {
    fun insertShellyAction() {
        val shellyTisch = Shelly("Tisch", "http://192.168.178.37/")
        val shellyBett = Shelly("Bett", "http://192.168.178.36/")
        val color = ColorMix(255, 0, 0, 0, 100)
        val shellyAction = ShellyAction(
            mutableListOf(shellyBett, shellyTisch),
            isEnabled = true,
            isLight = true,
            color
        )
        repo.insertShellyAction(shellyAction)
    }
}