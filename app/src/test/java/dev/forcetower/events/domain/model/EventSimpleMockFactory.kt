package dev.forcetower.events.domain.model

import dev.forcetower.events.tooling.MockFactory
import java.time.LocalDateTime
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