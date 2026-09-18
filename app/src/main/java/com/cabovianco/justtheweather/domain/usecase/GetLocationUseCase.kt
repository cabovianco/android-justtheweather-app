package com.cabovianco.justtheweather.domain.usecase

import com.cabovianco.justtheweather.domain.model.Location
import com.cabovianco.justtheweather.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLocationUseCase @Inject constructor(
    private val repository: LocationRepository
) {
    operator fun invoke(): Flow<Location?> = repository.current()
}
