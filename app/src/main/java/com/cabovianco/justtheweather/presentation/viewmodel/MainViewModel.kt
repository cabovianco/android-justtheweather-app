package com.cabovianco.justtheweather.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabovianco.justtheweather.domain.usecase.GetForecastUseCase
import com.cabovianco.justtheweather.domain.usecase.GetLocationUseCase
import com.cabovianco.justtheweather.domain.usecase.ShouldAskForPermissionsUseCase
import com.cabovianco.justtheweather.presentation.state.MainState
import com.cabovianco.justtheweather.presentation.state.MainUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val shouldAskForPermissionsUseCase: ShouldAskForPermissionsUseCase,
    private val getLocationUseCase: GetLocationUseCase,
    private val getForecastUseCase: GetForecastUseCase
) : ViewModel() {
    private val _refresh: MutableSharedFlow<Unit> = MutableSharedFlow(replay = 1)
    private val _askForPermissions: MutableSharedFlow<Unit> = MutableSharedFlow(replay = 1)
    val askForPermissions: SharedFlow<Unit> get() = _askForPermissions.asSharedFlow()

    init {
        if (shouldAskForPermissionsUseCase()) _askForPermissions.tryEmit(Unit)

        _refresh.tryEmit(Unit)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<MainUiState> = _refresh
        .flatMapLatest { getLocationUseCase() }
        .map { location ->
            if (location == null) return@map MainUiState(state = MainState.Loading)

            getForecastUseCase(latitude = location.latitude, longitude = location.longitude)
                .fold(
                    onSuccess = {
                        MainUiState(
                            city = location.city,
                            updatedAt = it.current.date,
                            state = MainState.Success(forecast = it)
                        )
                    },
                    onFailure = {
                        MainUiState(
                            city = location.city,
                            state = MainState.Error
                        )
                    }
                )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MainUiState()
        )

    fun onRefresh() {
        viewModelScope.launch {
            _refresh.emit(Unit)
        }
    }
}
