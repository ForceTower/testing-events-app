package dev.forcetower.events.core.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import dev.forcetower.events.core.utils.DateConverters
import java.time.LocalDateTime

@Entity
@TypeConverters(value = [DateConverters::class])
data class Event(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val image: String,
    val date: LocalDateTime,
    val latitude: Double,
    val longitude: Double,
    val price: Double
)
