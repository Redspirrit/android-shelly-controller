package de.shelly_controller

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("color/0?")
    suspend fun triggerAction(@Query("turn") state: String)
}