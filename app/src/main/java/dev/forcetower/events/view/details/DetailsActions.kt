package dev.forcetower.events.view.details

import dev.forcetower.events.core.model.Event

interface DetailsActions {
    fun checkInEvent(event: Event)
}