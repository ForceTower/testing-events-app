package dev.forcetower.events.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.forcetower.events.data.local.dao.EventDao
import dev.forcetower.events.data.model.Event

@Database(
    entities = [
        Event::class
    ],
    version = 1
)
@TypeConverters(DateConverters::class)
abstract class EventDB : RoomDatabase() {
    abstract val events: EventDao
}