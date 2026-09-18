package com.cabovianco.justtheweather.data.remote.model

import com.cabovianco.justtheweather.data.remote.serializer.LocalDateSerializer
import com.cabovianco.justtheweather.data.remote.serializer.LocalDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalDateTime

@Serializable
data class ForecastResponse(
    @SerialName("current") val current: CurrentWeather,
    @SerialName("hourly") val hourly: HourlyWeather,
    @SerialName("daily") val daily: DailyWeather
)

@Serializable
data class CurrentWeather(
    @Serializable(with = LocalDateTimeSerializer::class)
    @SerialName("time") val time: LocalDateTime,
    @SerialName("temperature_2m") val temperature: Double,
    @SerialName("apparent_temperature") val apparentTemperature: Double,
    @SerialName("weather_code") val weatherCode: Int
)

@Serializable
data class HourlyWeather(
    @SerialName("time") val time: List<@Serializable(with = LocalDateTimeSerializer::class) LocalDateTime>,
    @SerialName("temperature_2m") val temperature: List<Double>,
    @SerialName("weather_code") val weatherCode: List<Int>
)

@Serializable
data class DailyWeather(
    @SerialName("time") val time: List<@Serializable(with = LocalDateSerializer::class) LocalDate>,
    @SerialName("temperature_2m_max") val temperatureMax: List<Double>,
    @SerialName("temperature_2m_min") val temperatureMin: List<Double>,
    @SerialName("weather_code") val weatherCode: List<Int>
)
