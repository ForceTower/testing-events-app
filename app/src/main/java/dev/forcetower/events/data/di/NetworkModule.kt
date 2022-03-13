package dev.forcetower.events.data.di

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.forcetower.events.data.Constants
import dev.forcetower.events.data.network.EventService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideEventService(gson: Gson): EventService {
        return Retrofit.Builder()
            .baseUrl(Constants.EVENTS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(EventService::class.java)
    }
}
