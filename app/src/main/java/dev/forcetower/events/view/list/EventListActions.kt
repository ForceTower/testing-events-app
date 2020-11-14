package dev.forcetower.events.view.list

import androidx.lifecycle.LiveData
import dev.forcetower.events.core.model.Event

interface EventListActions {
    val loading: LiveData<Boolean>

    fun updateList()
    fun onSelectEvent(event: Event)
}