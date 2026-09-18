package com.cabovianco.justtheweather.data.repository

import android.Manifest
import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.annotation.RequiresPermission
import com.cabovianco.justtheweather.data.source.LocationDataSource
import com.cabovianco.justtheweather.domain.model.Location
import com.cabovianco.justtheweather.domain.repository.LocationRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val source: LocationDataSource
) : LocationRepository {
    private val geocoder = Geocoder(context)

    companion object {
        private const val MAX_RESULTS = 1
        private const val TAG = "LocationRepositoryImpl"
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun current(): Flow<Location?> = source.current()
        .map {
            if (it == null) return@map null

            val latitude = it.latitude
            val longitude = it.longitude

            val addresses = geocoder.getFromLocation(
                latitude, longitude, MAX_RESULTS
            )

            Log.d(TAG, "Address: ${addresses?.firstOrNull()}")

            Location(
                latitude = latitude,
                longitude = longitude,
                city = addresses?.firstOrNull()?.locality
            )
        }
}
