package dev.forcetower.events.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import dev.forcetower.events.data.model.Event
import dev.forcetower.events.tooling.database.dao.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
abstract class EventDao : BaseDao<Event>() {
    @Query("SELECT * FROM Event ORDER BY id ASC")
    abstract fun list(): Flow<List<Event>>

    @Query("SELECT * FROM Event WHERE id = :id")
    abstract fun getById(id: String): Flow<Event>
}
