package dev.forcetower.events.presentation.checkin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dev.forcetower.events.R
import dev.forcetower.events.domain.usecase.CheckInUseCase
import dev.forcetower.events.domain.validator.Validator
import dev.forcetower.events.tooling.lifecycle.Event
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CheckInViewModel @AssistedInject constructor(
    @Assisted private val eventId: String,
    private val checkInUseCase: CheckInUseCase,
    private val validator: Validator<String>
) : ViewModel() {
    val name = MutableLiveData("")
    val email = MutableLiveData("")

    private val _currentState = MutableLiveData(CheckInState.FILL)

    private val _nameError = MutableLiveData<Int?>()
    val nameError: LiveData<Int?> = _nameError

    private val _emailError = MutableLiveData<Int?>()
    val emailError: LiveData<Int?> = _emailError

    private val _onCompleted = MutableLiveData<Event<Unit>>()
    val onCompleted: LiveData<Event<Unit>> = _onCompleted

    val loading = _currentState.map { it == CheckInState.LOADING }
    val completed = _currentState.map { it == CheckInState.COMPLETED }
    val error = _currentState.map { it == CheckInState.ERROR }
    val filling = _currentState.map { it == CheckInState.FILL }

    fun checkIn() {
        val name = name.value?.trim() ?: return
        val email = email.value?.trim() ?: return

        val nameErrorCheck = when {
            name.isBlank() -> R.string.check_in_name_is_required
            name.trim().length < 4 -> R.string.check_in_name_too_short
            else -> null
        }

        val emailErrorCheck = when {
            email.isBlank() -> R.string.check_in_email_is_required
            !validator.isValid(email) -> R.string.check_in_email_invalid
            else -> null
        }

        _nameError.value = nameErrorCheck
        _emailError.value = emailErrorCheck

        if (nameErrorCheck != null || emailErrorCheck != null) return

        viewModelScope.launch {
            _currentState.value = CheckInState.LOADING
            try {
                checkInUseCase(name, email, eventId)
                _currentState.value = CheckInState.COMPLETED
                delay(2500)
                _onCompleted.value = Event(Unit)
            } catch (error: Throwable) {
                _currentState.value = CheckInState.ERROR
            }
        }
    }

    fun checkInAgain() {
        _currentState.value = CheckInState.FILL
    }
}

@AssistedFactory
interface CheckInViewModelFactory {
    fun create(eventId: String): CheckInViewModel
}

fun provideFactory(
    assistedFactory: CheckInViewModelFactory,
    eventId: String
) = object : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return assistedFactory.create(eventId) as T
    }
}
