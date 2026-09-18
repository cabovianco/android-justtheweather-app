package com.cabovianco.justtheweather.domain.model

import java.time.LocalDateTime

data class Forecast(
    val current: WeatherEntry,
    val hourly: List<WeatherEntry>,
    val daily: List<WeatherEntry>
)

data class WeatherEntry(
    val date: LocalDateTime,
    val condition: WeatherCondition,
    val temperature: WeatherTemperature
)

enum class WeatherCondition {
    SUN,
    CLOUDY,
    RAIN,
    STORM,
    SNOW,
    UNKNOWN
}

sealed interface WeatherTemperature {
    data class Current(val value: Int, val apparent: Int): WeatherTemperature
    data class Hourly(val value: Int): WeatherTemperature
    data class Daily(val min: Int, val max: Int): WeatherTemperature
}
