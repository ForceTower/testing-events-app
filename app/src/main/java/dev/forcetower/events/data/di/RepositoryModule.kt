package dev.forcetower.events.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.forcetower.events.data.repository.EventRepository
import dev.forcetower.events.data.repository.EventRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun event(impl: EventRepositoryImpl): EventRepository
}
