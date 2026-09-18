package com.cabovianco.justtheweather.data.mapper

import com.cabovianco.justtheweather.data.remote.model.CurrentWeather
import com.cabovianco.justtheweather.data.remote.model.DailyWeather
import com.cabovianco.justtheweather.data.remote.model.ForecastResponse
import com.cabovianco.justtheweather.data.remote.model.HourlyWeather
import com.cabovianco.justtheweather.domain.model.Forecast
import com.cabovianco.justtheweather.domain.model.WeatherCondition
import com.cabovianco.justtheweather.domain.model.WeatherEntry
import com.cabovianco.justtheweather.domain.model.WeatherTemperature

fun ForecastResponse.toDomain(): Forecast = Forecast(
    current = current.toDomain(),
    hourly = hourly.toDomain(),
    daily = daily.toDomain()
)

private fun CurrentWeather.toDomain(): WeatherEntry = WeatherEntry(
    date = time,
    condition = weatherCodeToCondition(weatherCode),
    temperature = WeatherTemperature.Current(
        temperature.toInt(), apparentTemperature.toInt()
    )
)

private fun HourlyWeather.toDomain(): List<WeatherEntry> = time.mapIndexed { index, date ->
    WeatherEntry(
        date = date,
        condition = weatherCodeToCondition(weatherCode[index]),
        temperature = WeatherTemperature.Hourly(temperature[index].toInt())
    )
}

private fun DailyWeather.toDomain(): List<WeatherEntry> = time.mapIndexed { index, date ->
    WeatherEntry(
        date = date.atStartOfDay(),
        condition = weatherCodeToCondition(weatherCode[index]),
        temperature = WeatherTemperature.Daily(
            min = temperatureMin[index].toInt(),
            max = temperatureMax[index].toInt()
        )
    )
}

private fun weatherCodeToCondition(code: Int): WeatherCondition = when(code) {
    0 -> WeatherCondition.SUN
    1, 2, 3, 45, 48 -> WeatherCondition.CLOUDY
    51, 53, 55, 61, 63, 65, 80, 81, 82 -> WeatherCondition.RAIN
    56, 57, 66, 67, 71, 73, 75, 77, 85, 86 -> WeatherCondition.SNOW
    95, 96, 99 -> WeatherCondition.STORM
    else -> WeatherCondition.UNKNOWN
}
