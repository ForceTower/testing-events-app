package dev.forcetower.events.presentation.details

import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dev.forcetower.events.R
import dev.forcetower.events.domain.model.CompleteEvent
import dev.forcetower.events.domain.usecase.GetEventUseCase
import dev.forcetower.events.domain.usecase.UpdateEventUseCase
import dev.forcetower.events.tooling.lifecycle.Event
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class DetailsViewModel @AssistedInject constructor(
    @Assisted private val eventId: String,
    getEventUseCase: GetEventUseCase,
    private val refreshUseCase: UpdateEventUseCase
) : ViewModel() {
    val event = getEventUseCase(eventId).asLiveData()

    private val _onCheckIn = MutableLiveData<Event<String>>()
    val onCheckIn: LiveData<Event<String>> = _onCheckIn

    private val _onRefreshFailed = MutableLiveData<Event<Unit>>()
    val onRefreshFailed: LiveData<Event<Unit>> = _onRefreshFailed

    private val _onShareInfo = MutableLiveData<Event<String>>()
    val onShareInfo: LiveData<Event<String>> = _onShareInfo

    private val _onOpenMap = MutableLiveData<Event<Pair<Double, Double>>>()
    val onOpenMap: LiveData<Event<Pair<Double, Double>>> = _onOpenMap

    init { refresh() }

    private fun refresh() {
        viewModelScope.launch {
            try {
                refreshUseCase(eventId)
            } catch (error: Throwable) {
                _onRefreshFailed.value = Event(Unit)
            }
        }
    }

    fun onMenuItemClicked(itemId: Int, event: CompleteEvent?): Boolean {
        event ?: return true

        when (itemId) {
            R.id.item_share -> onShare(event)
            R.id.item_map -> onShowMap(event)
        }

        return true
    }

    private fun onShare(event: CompleteEvent) {
        val content = """
            ${event.title}
            ${event.date.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT))}
        """.trimIndent()
        _onShareInfo.value = Event(content)
    }

    private fun onShowMap(event: CompleteEvent) {
        _onOpenMap.value = Event(event.latitude to event.longitude)
    }

    fun checkInEvent(event: CompleteEvent?) {
        event ?: return
        _onCheckIn.value = Event(event.id)
    }
}

@AssistedFactory
interface DetailsViewModelFactory {
    fun create(eventId: String): DetailsViewModel
}

fun provideFactory(
    assistedFactory: DetailsViewModelFactory,
    eventId: String
) = object : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return assistedFactory.create(eventId) as T
    }
}