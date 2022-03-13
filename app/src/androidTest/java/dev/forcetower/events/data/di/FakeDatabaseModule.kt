package dev.forcetower.events.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dev.forcetower.events.data.local.EventDB
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
object FakeDatabaseModule {
    @Singleton
    @Provides
    fun provideEventDatabase(@ApplicationContext context: Context): EventDB {
        return Room.inMemoryDatabaseBuilder(context, EventDB::class.java).build()
    }
}