package com.cabovianco.justtheweather.domain.repository

interface PermissionRepository {
    fun hasLocationPermission(): Boolean
}
