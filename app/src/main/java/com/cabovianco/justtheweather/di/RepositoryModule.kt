package com.cabovianco.justtheweather.di

import com.cabovianco.justtheweather.data.repository.LocationRepositoryImpl
import com.cabovianco.justtheweather.data.repository.PermissionRepositoryImpl
import com.cabovianco.justtheweather.data.repository.WeatherRepositoryImpl
import com.cabovianco.justtheweather.domain.repository.LocationRepository
import com.cabovianco.justtheweather.domain.repository.PermissionRepository
import com.cabovianco.justtheweather.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindPermissionRepository(
        impl: PermissionRepositoryImpl
    ): PermissionRepository

    @Binds
    abstract fun bindLocationRepository(
        impl: LocationRepositoryImpl
    ): LocationRepository

    @Binds
    abstract fun bindWeatherRepository(
        impl: WeatherRepositoryImpl
    ): WeatherRepository
}
