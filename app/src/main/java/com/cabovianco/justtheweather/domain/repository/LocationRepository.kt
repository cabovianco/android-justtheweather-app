package com.cabovianco.justtheweather.domain.repository

import com.cabovianco.justtheweather.domain.model.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun current(): Flow<Location?>
}
