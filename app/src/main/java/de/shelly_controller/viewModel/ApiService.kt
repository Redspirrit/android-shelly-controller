package de.shelly_controller.viewModel

import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiService {
    @GET("color/0")
    suspend fun triggerAction(@QueryMap params: Map<String, String>)
}