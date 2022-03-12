package dev.forcetower.events.domain.usecase

import dagger.Reusable
import dev.forcetower.events.data.repository.EventRepository
import javax.inject.Inject

@Reusable
class UpdateEventUseCase @Inject constructor(
    private val repository: EventRepository
) {
    suspend operator fun invoke(id: String) {
        repository.updateEvent(id)
    }
}