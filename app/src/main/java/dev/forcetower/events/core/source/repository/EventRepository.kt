package dev.forcetower.events.core.source.repository

import dev.forcetower.events.core.model.CheckInData
import dev.forcetower.events.core.source.local.EventDB
import dev.forcetower.events.core.source.remote.EventService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepository @Inject constructor(
    private val database: EventDB,
    private val service: EventService
) {
    fun getEvents() = database.events().getEvents()

    suspend fun updateEvents() = withContext(Dispatchers.IO) {
        try {
            val events = service.events()
            database.events().insertOrUpdate(events)
            true
        } catch (error: Exception) {
            Timber.e(error, "Something wrong happened")
            false
        }
    }

    fun getEvent(id: String) = database.events().getEventById(id)

    suspend fun updateEvent(id: String) = withContext(Dispatchers.IO) {
        try {
            val event = service.event(id)
            database.events().insert(event)
            true
        } catch (error: Exception) {
            Timber.e(error, "Something wrong happened")
            false
        }
    }

    suspend fun checkIn(eventId: String, name: String, email: String) = withContext(Dispatchers.IO) {
        try {
            val data = CheckInData(eventId, name, email)
            service.checkIn(data)
            true
        } catch (error: Exception) {
            false
        }
    }
}
