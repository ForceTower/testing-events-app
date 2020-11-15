package dev.forcetower.events.core.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.forcetower.events.core.model.Event
import dev.forcetower.events.core.source.local.dao.EventDao

@Database(
    entities = [
        Event::class
    ],
    version = 1
)
abstract class EventDB : RoomDatabase() {
    abstract fun events(): EventDao
}
