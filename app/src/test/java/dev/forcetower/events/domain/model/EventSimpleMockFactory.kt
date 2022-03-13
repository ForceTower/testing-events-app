package dev.forcetower.events.domain.model

import dev.forcetower.events.data.mock.MockFactory
import java.util.UUID

object EventSimpleMockFactory : MockFactory<SimpleEvent> {
    override fun create(): SimpleEvent {
        return SimpleEvent(
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString()
        )
    }
}