package dev.forcetower.events.domain.mappers

import dev.forcetower.events.data.model.Event
import dev.forcetower.events.domain.model.CompleteEvent
import dev.forcetower.events.domain.model.SimpleEvent

fun Event.toDomainSimple() = SimpleEvent(
    id,
    title,
    description,
    image,
    image != null,
    description.isNotBlank()
)

fun Event.toDomainComplete() = CompleteEvent(
    id,
    title,
    description,
    image,
    date,
    latitude,
    longitude,
    price
)
