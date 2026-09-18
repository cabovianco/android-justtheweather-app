package com.cabovianco.justtheweather.data.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.cabovianco.justtheweather.domain.repository.PermissionRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PermissionRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : PermissionRepository {
    override fun hasLocationPermission(): Boolean =
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
}
