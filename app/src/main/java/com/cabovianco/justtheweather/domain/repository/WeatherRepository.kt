package com.cabovianco.justtheweather.domain.repository

import com.cabovianco.justtheweather.domain.model.Forecast

interface WeatherRepository {
    suspend fun currentForecast(latitude: Double, longitude: Double): Result<Forecast>
}
