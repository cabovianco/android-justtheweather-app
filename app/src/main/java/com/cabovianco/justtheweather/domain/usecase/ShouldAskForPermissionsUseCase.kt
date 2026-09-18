package com.cabovianco.justtheweather.domain.usecase

import android.util.Log
import com.cabovianco.justtheweather.domain.repository.PermissionRepository
import javax.inject.Inject

class ShouldAskForPermissionsUseCase @Inject constructor(
    private val repository: PermissionRepository
) {
    operator fun invoke(): Boolean {
        Log.d("ShouldAskForPermissionsUseCase", "invoke: ${repository.hasLocationPermission()}")
        return !repository.hasLocationPermission()
    }
}
