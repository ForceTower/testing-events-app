package dev.forcetower.events.domain.usecase

import dagger.Reusable
import dev.forcetower.events.data.repository.EventRepository
import dev.forcetower.events.domain.mappers.toDomainComplete
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@Reusable
class GetEventUseCase @Inject constructor(
    private val repository: EventRepository
) {
    operator fun invoke(id: String) = repository.getEvent(id).map { it.toDomainComplete() }
}
