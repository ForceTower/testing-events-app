package dev.forcetower.events.core.source.local

import androidx.annotation.VisibleForTesting
import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.sqlite.db.SupportSQLiteOpenHelper
import dev.forcetower.events.core.source.local.dao.EventDao
import io.mockk.mockk

class FakeEventDB : EventDB() {
    @VisibleForTesting
    val events = mockk<EventDao>()

    override fun events() = events
    override fun createOpenHelper(config: DatabaseConfiguration?) = mockk<SupportSQLiteOpenHelper>()
    override fun createInvalidationTracker() = mockk<InvalidationTracker>()
    override fun clearAllTables() {}
}