package dev.forcetower.events.data.repository

import dev.forcetower.events.data.local.EventDB
import dev.forcetower.events.data.model.dto.CheckInRequest
import dev.forcetower.events.data.network.EventService
import dev.forcetower.events.domain.validator.Validator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepositoryImpl @Inject constructor(
    private val database: EventDB,
    private val service: EventService,
    private val validator: Validator<String>
) : EventRepository {
    override fun getEvents() = database.events.list()
    override fun getEvent(id: String) = database.events.getById(id)

    override suspend fun checkIn(email: String, name: String, eventId: String) {
        if (!validator.isValid(email)) throw IllegalArgumentException("Argument is not a valid email.")
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
