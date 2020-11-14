package dev.forcetower.events.view.list

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dev.forcetower.events.R
import dev.forcetower.events.core.model.Event
import dev.forcetower.events.core.source.repository.EventRepository
import dev.forcetower.toolkit.lifecycle.LiveEvent
import kotlinx.coroutines.launch

class EventListViewModel @ViewModelInject constructor(
    private val repository: EventRepository
) : ViewModel(), EventListActions {
    private val _onEventSelected = MutableLiveData<LiveEvent<Event>>()
    val onEventSelected: LiveData<LiveEvent<Event>> = _onEventSelected

    private val _onUpdateError = MutableLiveData<LiveEvent<Int>>()
    val onUpdateError: LiveData<LiveEvent<Int>> = _onUpdateError

    private val _loading = MutableLiveData(false)
    override val loading: LiveData<Boolean> = _loading

    private var _eventsSource: LiveData<List<Event>>? = null
    private var _eventSource: LiveData<Event>? = null
    private var _currentEventId: String? = null

    fun getEvents(): LiveData<List<Event>> {
        val source = _eventsSource
        if (source != null) return source
        val next = repository.getEvents().asLiveData()
        _eventsSource = next
        return next
    }

    fun getEvent(id: String): LiveData<Event> {
        val source = _eventSource
        if (source != null && _currentEventId == id) return source
        val next = repository.getEvent(id).asLiveData()
        _eventSource = next
        return next
    }

    fun updateEvent(id: String) {
        if (_loading.value != true) {
            viewModelScope.launch {
                _loading.value = true
                val updated = repository.updateEvent(id)
                if (!updated) {
                    _onUpdateError.value = LiveEvent(R.string.update_list_error)
                }
                _loading.value = false
            }
        }
    }

    override fun updateList() {
        if (_loading.value != true) {
            viewModelScope.launch {
                _loading.value = true
                val updated = repository.updateEvents()
                if (!updated) {
                    _onUpdateError.value = LiveEvent(R.string.update_list_error)
                }
                _loading.value = false
            }
        }
    }

    override fun onSelectEvent(event: Event) {
        _onEventSelected.value = LiveEvent(event)
    }
}