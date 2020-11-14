package dev.forcetower.events.core.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import dev.forcetower.events.core.model.Event
import dev.forcetower.toolkit.database.dao.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
abstract class EventDao : BaseDao<Event>() {
    @Query("SELECT * FROM Event")
    abstract fun getEvents(): Flow<List<Event>>

    @Query("SELECT * FROM Event WHERE id = :id")
    abstract fun getEventById(id: String): Flow<Event>

    @Query("SELECT * FROM Event WHERE id = :id")
    abstract suspend fun getEventByIdDirect(id: String): Event?

    override suspend fun getValueByIDDirect(value: Event): Event? {
        return getEventByIdDirect(value.id)
    }
}