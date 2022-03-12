package dev.forcetower.events.data.model.dto

data class CheckInRequest(
    val eventId: String,
    val name: String,
    val email: String
)
