package dev.forcetower.events.data.repository

import dev.forcetower.events.data.local.EventDB
import dev.forcetower.events.data.model.dto.CheckInRequest
import dev.forcetower.events.data.network.EventService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepositoryImpl @Inject constructor(
    private val database: EventDB,
    private val service: EventService
) : EventRepository {
    override fun getEvents() = database.events.list()
    override fun getEvent(id: String) = database.events.getById(id)

    override suspend fun checkIn(email: String, name: String, eventId: String) {
        val request = CheckInRequest(eventId, name, email)
        service.checkIn(request)
    }

    override suspend fun updateEvents() {
        val data = service.events()
        database.events.upsert(data)
    }

    override suspend fun updateEvent(id: String) {
        val data = service.event(id)
        database.events.upsert(data)
    }
}
