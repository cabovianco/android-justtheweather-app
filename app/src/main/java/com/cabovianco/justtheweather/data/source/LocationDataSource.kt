package com.cabovianco.justtheweather.data.source

import android.Manifest
import android.content.Context
import android.location.Location
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class LocationDataSource @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private val client = LocationServices.getFusedLocationProviderClient(context)

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun current(): Flow<Location?> = callbackFlow {
        client.lastLocation
            .addOnSuccessListener { location ->
                trySend(location)
            }
            .addOnFailureListener { e ->
                trySend(null)
            }
            .addOnCanceledListener {
                trySend(null)
            }

        awaitClose()
    }
}
