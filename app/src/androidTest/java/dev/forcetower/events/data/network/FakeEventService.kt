package dev.forcetower.events.data.network

import dev.forcetower.events.data.mock.EventMockFactory
import dev.forcetower.events.data.model.Event
import dev.forcetower.events.data.model.dto.CheckInRequest
import kotlinx.coroutines.delay
import java.io.IOException
import java.util.concurrent.atomic.AtomicBoolean

class FakeEventService : EventService {
    private var delayAllRequests = 0L
    private var fallen = false
    private val fails = AtomicBoolean(false)

    val events = EventMockFactory.createList(30)

    fun failNextRequest() {
        fails.set(true)
    }

    fun failEveryRequest(fail: Boolean) {
        fails.set(fail)
        fallen = fail
    }

    fun setNetworkDelay(delay: Long) {
        delayAllRequests = delay
    }

    override suspend fun events(): List<Event> {
        checkRequest()
        return events
    }

    override suspend fun event(id: String): Event {
        checkRequest()
        return events.first { it.id == id }
    }

    override suspend fun checkIn(data: CheckInRequest) {
        checkRequest()
    }

    private suspend fun checkRequest() {
        delay(delayAllRequests)
        if (fails.getAndSet(fallen)) {
            throw IOException("request failed.")
        }
    }
}
