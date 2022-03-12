package dev.forcetower.events.data.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.forcetower.events.data.Constants
import dev.forcetower.events.data.local.EventDB
import dev.forcetower.events.data.network.EventService
import dev.forcetower.events.data.network.GsonSerializers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Singleton
    @Provides
    fun provideEventService(gson: Gson): EventService {
        return Retrofit.Builder()
            .baseUrl(Constants.EVENTS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(EventService::class.java)
    }

    @Singleton
    @Provides
    fun provideEventDatabase(@ApplicationContext context: Context): EventDB {
        return Room.databaseBuilder(context, EventDB::class.java, Constants.DATABASE_FILE_NAME).build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, GsonSerializers.LDT_DESERIALIZER)
            .serializeNulls()
            .create()
    }
}