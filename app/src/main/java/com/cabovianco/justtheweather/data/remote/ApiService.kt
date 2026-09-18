package com.cabovianco.justtheweather.data.remote

import com.cabovianco.justtheweather.data.remote.model.ForecastResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("forecast?timezone=auto&current=temperature_2m,weather_code,apparent_temperature&hourly=temperature_2m,weather_code&daily=weather_code,temperature_2m_max,temperature_2m_min")
    suspend fun getForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): ForecastResponse
}
