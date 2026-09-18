package com.cabovianco.justtheweather.presentation.state

import com.cabovianco.justtheweather.domain.model.Forecast
import java.time.LocalDateTime

data class MainUiState(
    val city: String? = null,
    val updatedAt: LocalDateTime? = null,
    val state: MainState = MainState.Loading
)

sealed interface MainState {
    data class Success(val forecast: Forecast) : MainState
    data object Loading : MainState
    data object Error : MainState
}
