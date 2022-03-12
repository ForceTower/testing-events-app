package dev.forcetower.events.domain.model

import java.time.LocalDateTime

data class CompleteEvent(
    val id: String,
    val title: String,
    val description: String,
    val image: String?,
    val date: LocalDateTime,
    val latitude: Double,
    val longitude: Double,
    val price: Double
)
