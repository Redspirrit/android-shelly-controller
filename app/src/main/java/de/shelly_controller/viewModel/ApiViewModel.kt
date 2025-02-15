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
    private var message by mutableStateOf("")
    private var apiService: ApiService? = null

    fun setBaseUrl(url: String) {
        apiService = RetrofitFactory.create(url)
    }

    fun ledAction(params: Map<String, String>) {
        viewModelScope.launch {
            try {
                apiService!!.triggerAction(params)
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