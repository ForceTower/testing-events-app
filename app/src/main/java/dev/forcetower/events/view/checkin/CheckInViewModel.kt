package dev.forcetower.events.view.checkin

import android.util.Patterns
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.forcetower.events.R
import dev.forcetower.events.core.model.Event
import dev.forcetower.events.core.source.repository.EventRepository
import dev.forcetower.toolkit.lifecycle.LiveEvent
import kotlinx.coroutines.launch
import timber.log.Timber

class CheckInViewModel @ViewModelInject constructor(
    private val repository: EventRepository
) : ViewModel(), CheckInActions {
    private var _eventId: String? = null

    override val name = MutableLiveData("")
    override val email = MutableLiveData("")

    private val _loading = MutableLiveData(false)
    override val loading: LiveData<Boolean> = _loading

    private val _nameError = MutableLiveData<Int?>(null)
    override val nameError: LiveData<Int?> = _nameError

    private val _emailError = MutableLiveData<Int?>(null)
    override val emailError: LiveData<Int?> = _emailError

    private val _onRegistered = MutableLiveData<LiveEvent<Unit>>()
    val onRegistered: LiveData<LiveEvent<Unit>> = _onRegistered

    override fun checkIn() {
        val eventId = _eventId
        if (eventId == null) {
            Timber.e("Event Id is null, did you forget to set it?")
            return
        }
        if (_loading.value == true) return

        val name = name.value?.trim() ?: return
        val email = email.value?.trim() ?: return

        val nameErrorCheck = when {
            name.isBlank() -> R.string.name_is_required
            name.length < 3 ->  R.string.name_too_short
            else -> null
        }

        val emailErrorCheck = when {
            email.isBlank() -> R.string.email_is_required
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> R.string.email_invalid
            else -> null
        }

        _nameError.value = nameErrorCheck
        _emailError.value = emailErrorCheck

        if (nameErrorCheck != null || emailErrorCheck != null) return

        viewModelScope.launch {
            _loading.value = true
            val registered = repository.checkIn(eventId, name, email)
            if (registered) _onRegistered.value = LiveEvent(Unit)
            _loading.value = false
        }
    }

    fun setEventId(id: String) {
        _eventId = id
    }
}