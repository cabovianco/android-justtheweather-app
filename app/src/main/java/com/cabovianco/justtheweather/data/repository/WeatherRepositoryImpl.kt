package com.cabovianco.justtheweather.data.repository

import android.util.Log
import com.cabovianco.justtheweather.data.mapper.toDomain
import com.cabovianco.justtheweather.data.remote.ApiService
import com.cabovianco.justtheweather.domain.model.Forecast
import com.cabovianco.justtheweather.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val service: ApiService
) : WeatherRepository {
    companion object {
        private const val TAG = "WeatherRepository"
    }

    override suspend fun currentForecast(
        latitude: Double,
        longitude: Double
    ): Result<Forecast> = try {
        val response = service.getForecast(latitude, longitude)

        Result.success(response.toDomain())

    } catch (ex: Exception) {
        Log.e(TAG, "currentForecast", ex)
        Result.failure(ex)
    }
}
