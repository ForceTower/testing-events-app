package dev.forcetower.events.view.list

import androidx.lifecycle.LiveData
import dev.forcetower.events.core.model.Event

interface EventListActions {
    val loading: LiveData<Boolean>

    fun updateList(forcedUpdate: Boolean)
    fun onSelectEvent(event: Event)
}