package dev.forcetower.events.core.model

import java.time.LocalDateTime
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random

object EventFakeDataFactory {
    val EVENT_DEFAULT = Event(
        "1",
        "This is an event! ## 1",
        "This is not a description whatsoever. but it is indeed a really long description, one that you never seen before...\n\nYes... More \\n's moreeeeeeeeee\n\nThat's it",
        "https://images.unsplash.com/photo-1605405809561-2ec85c966680?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1350&q=80",
        LocalDateTime.now(),
        37.4220656,
        122.0862784,
        76.98
    )

    val EVENT_LIST = listOf(
        EVENT_DEFAULT,
        EVENT_DEFAULT,
        EVENT_DEFAULT
    )

    private val random = Random.Default
    private val counter = AtomicInteger(0)

    fun makeEvent(): Event {
        val id = counter.addAndGet(1)
        return EVENT_DEFAULT.copy(
            id = id.toString(),
            price = random.nextDouble(1.0, 99.0)
        )
    }

    fun makeEventList(size: Int = 4): List<Event> {
        return (0..size).map { makeEvent() }
    }
}
