package dev.forcetower.events.domain.model

data class SimpleEvent(
    val id: String,
    val name: String,
    val description: String,
    val image: String?
)
