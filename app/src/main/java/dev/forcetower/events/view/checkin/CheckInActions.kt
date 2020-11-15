package dev.forcetower.events.view.checkin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface CheckInActions {
    val name: MutableLiveData<String>
    val email: MutableLiveData<String>
    val loading: LiveData<Boolean>
    val nameError: LiveData<Int?>
    val emailError: LiveData<Int?>

    fun checkIn()
}
