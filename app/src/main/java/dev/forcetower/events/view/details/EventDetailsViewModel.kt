package dev.forcetower.events.view.details

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dev.forcetower.events.core.model.Event
import dev.forcetower.events.core.source.repository.EventRepository
import dev.forcetower.toolkit.lifecycle.LiveEvent
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.format.DateTimeFormatter

class EventDetailsViewModel @ViewModelInject constructor(
    private val repository: EventRepository
) : ViewModel(), DetailsActions {
    private var _eventSource: LiveData<Event>? = null
    private var _currentEventId: String? = null

    private val _onCheckIn = MutableLiveData<LiveEvent<Event>>()
    val onCheckIn: LiveData<LiveEvent<Event>> = _onCheckIn

    fun getEvent(id: String): LiveData<Event> {
        val source = _eventSource
        if (source != null && _currentEventId == id) return source
        val next = repository.getEvent(id).asLiveData()
        _eventSource = next
        return next
    }

    fun updateEvent(id: String) {
        viewModelScope.launch {
            repository.updateEvent(id)
        }
    }

    fun getShareText(): String {
        val event = _eventSource?.value ?: return ""
        val pattern = "EEE, dd MMMM yyyy HH:mm"
        return "${event.title}\n${event.date.format(DateTimeFormatter.ofPattern(pattern))}"
    }

    override fun checkInEvent(event: Event) {
        Timber.d("On check in $event")
        _onCheckIn.value = LiveEvent(event)
    }
}