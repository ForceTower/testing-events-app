package dev.forcetower.events.presentation.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.forcetower.events.domain.model.SimpleEvent
import dev.forcetower.events.domain.usecase.GetEventsUseCase
import dev.forcetower.events.domain.usecase.UpdateEventsUseCase
import dev.forcetower.events.tooling.lifecycle.Event
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    getEventsUseCase: GetEventsUseCase,
    private val refreshUseCase: UpdateEventsUseCase
) : ViewModel() {
    private val _events = getEventsUseCase()
    val events = _events.asLiveData()

    private val _onEventSelected = MutableLiveData<Event<String>>()
    val onEventSelected: LiveData<Event<String>> = _onEventSelected

    private val _onRefreshFailed = MutableLiveData<Event<Unit>>()
    val onRefreshFailed: LiveData<Event<Unit>> = _onRefreshFailed

    private val _refreshing = MutableLiveData(false)
    val refreshing = _refreshing

    val showError = combine(_events, refreshing.asFlow()) { list, loading ->
        list.isEmpty() && !loading
    }.asLiveData()

    init { refresh() }

    fun refresh() {
        if (_refreshing.value == true) return
        viewModelScope.launch {
            _refreshing.value = true
            try {
                refreshUseCase()
            } catch (error: Throwable) {
                _onRefreshFailed.value = Event(Unit)
            }
            _refreshing.value = false
        }
    }

    fun onEventClicked(event: SimpleEvent?) {
        event ?: return
        _onEventSelected.value = Event(event.id)
    }
}
