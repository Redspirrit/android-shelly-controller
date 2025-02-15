package de.shelly_controller.viewModel

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiService {
    @GET("color/0")
    suspend fun triggerAction(
        @Query("turn") turn: String,
        @Query("red") red: String,
        @Query("green") green: String,
        @Query("blue") blue: String,
        @Query("white") white: String,
        @Query("gain") gain: String
    )
}