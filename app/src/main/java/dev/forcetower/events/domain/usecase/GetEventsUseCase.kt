package dev.forcetower.events.domain.usecase

import dagger.Reusable
import dev.forcetower.events.data.repository.EventRepository
import dev.forcetower.events.domain.mappers.toDomainSimple
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@Reusable
class GetEventsUseCase @Inject constructor(
    private val repository: EventRepository
) {
    operator fun invoke() = repository.getEvents().map {
        it.map { el -> el.toDomainSimple() }
    }
}
