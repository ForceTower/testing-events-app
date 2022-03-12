package dev.forcetower.events.data.repository

import dev.forcetower.events.data.model.Event
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    fun getEvents(): Flow<List<Event>>
    fun getEvent(id: String): Flow<Event>
    suspend fun checkIn(email: String, name: String, eventId: String)
    suspend fun updateEvents()
    suspend fun updateEvent(id: String)
}