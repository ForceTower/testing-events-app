package dev.forcetower.events.dagger

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.forcetower.events.core.source.local.EventDB
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideEventDatabase(@ApplicationContext context: Context): EventDB {
        return Room.databaseBuilder(context, EventDB::class.java, "events.db").build()
    }
}