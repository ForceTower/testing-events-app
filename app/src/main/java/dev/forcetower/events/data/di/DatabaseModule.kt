package dev.forcetower.events.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.forcetower.events.data.Constants
import dev.forcetower.events.data.local.EventDB
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideEventDatabase(@ApplicationContext context: Context): EventDB {
        return Room.databaseBuilder(context, EventDB::class.java, Constants.DATABASE_FILE_NAME).build()
    }
}
