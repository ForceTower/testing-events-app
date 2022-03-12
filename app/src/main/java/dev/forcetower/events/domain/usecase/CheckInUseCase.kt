package dev.forcetower.events.domain.usecase

import dagger.Reusable
import dev.forcetower.events.data.repository.EventRepository
import javax.inject.Inject

@Reusable
class CheckInUseCase @Inject constructor(
    private val repository: EventRepository
) {
    suspend operator fun invoke(name: String, email: String, eventId: String) {
        repository.checkIn(email, name, eventId)
    }
}
