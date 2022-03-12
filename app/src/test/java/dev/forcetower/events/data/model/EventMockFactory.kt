package dev.forcetower.events.data.model

import dev.forcetower.events.tooling.MockFactory
import java.time.LocalDateTime
import java.util.UUID

object EventMockFactory : MockFactory<Event> {
    override fun create(): Event {
        return Event(
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            LocalDateTime.now().plusDays((-2000L..2000L).random()),
            (-5000..5000).random().toDouble() / 13,
            (-5000..5000).random().toDouble() / 13,
            (1..5000).random().toDouble() / 13
        )
    }
}