package de.shelly_controller.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ApiViewModel : ViewModel() {
    var message by mutableStateOf("")
    fun fetchGet(state: String) {

        viewModelScope.launch {
            try {
                RetrofitInstance.api.triggerAction(state)
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