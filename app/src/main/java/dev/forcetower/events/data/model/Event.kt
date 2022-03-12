package dev.forcetower.events.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class Event(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val image: String?,
    val date: LocalDateTime,
    val latitude: Double,
    val longitude: Double,
    val price: Double
)