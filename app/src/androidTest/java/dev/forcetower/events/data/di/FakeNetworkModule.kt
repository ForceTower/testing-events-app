package dev.forcetower.events.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dev.forcetower.events.data.network.EventService
import dev.forcetower.events.data.network.FakeEventService
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NetworkModule::class]
)
object FakeNetworkModule {
    @Singleton
    @Provides
    fun provideEventService(): EventService {
        return FakeEventService()
    }
}