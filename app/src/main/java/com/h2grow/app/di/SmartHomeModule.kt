package com.h2grow.app.di

import com.h2grow.app.data.repository.InMemorySmartHomeRepository
import com.h2grow.app.domain.repository.SmartHomeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SmartHomeModule {

    @Binds
    @Singleton
    abstract fun bindSmartHomeRepository(
        repository: InMemorySmartHomeRepository
    ): SmartHomeRepository
}
