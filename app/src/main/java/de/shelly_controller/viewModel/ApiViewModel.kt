package de.shelly_controller.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.shelly_controller.model.ColorMix
import de.shelly_controller.model.Shelly
import de.shelly_controller.model.ShellyAction
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ApiViewModel : ViewModel() {
    private var message by mutableStateOf("")
    private var apiService: ApiService? = null

    fun initShellys(): ShellyAction {
        val shellyTisch = Shelly("Tisch", "http://192.168.178.37/")
        val shellyBett = Shelly("Bett", "http://192.168.178.36/")
        val color = ColorMix(255, 0, 0, 0, 100)
        return ShellyAction(
            mutableListOf(shellyBett, shellyTisch),
            isEnabled = true,
            isLight = true,
            color
        )
    }

    fun ledAction(shellyAction: ShellyAction) {
        for (shelly in shellyAction.shelly) {
            apiService = RetrofitFactory.create(shelly.ip)
            viewModelScope.launch {
                try {
                    val state = if (shellyAction.isLight) "on" else "off"
                    apiService!!.triggerAction(
                        state,
                        shellyAction.color.red.toString(),
                        shellyAction.color.green.toString(),
                        shellyAction.color.blue.toString(),
                        shellyAction.color.white.toString(),
                        shellyAction.color.gain.toString()
                    )
                    message = "Action successful"
                } catch (e: IOException) {
                    message = "Network Error"
                } catch (e: HttpException) {
                    message = "Sever error"
                }
                Log.d("API", message)
            }
        }
    }
}