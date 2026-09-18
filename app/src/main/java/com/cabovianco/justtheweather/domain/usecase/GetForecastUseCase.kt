package com.cabovianco.justtheweather.domain.usecase

import com.cabovianco.justtheweather.domain.model.Forecast
import com.cabovianco.justtheweather.domain.repository.WeatherRepository
import java.time.LocalDateTime
import javax.inject.Inject

class GetForecastUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(latitude: Double, longitude: Double): Result<Forecast> =
        if (latitude !in -90.0..90.0 || longitude !in -180.0..180.0) return Result.failure(
            IllegalArgumentException("Invalid coordinates.")
        )
        else repository.currentForecast(latitude, longitude).map { forecast ->
            val now = LocalDateTime.now()

            forecast.copy(
                hourly = forecast.hourly.filter {
                    it.date >= now && it.date <= now.plusHours(24)
                },
                daily = forecast.daily.filter {
                    it.date > now && it.date <= now.plusDays(7)
                }
            )
        }
}
