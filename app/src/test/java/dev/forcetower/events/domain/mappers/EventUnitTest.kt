package dev.forcetower.events.domain.mappers

import dev.forcetower.events.data.model.EventMockFactory
import dev.forcetower.events.domain.model.CompleteEvent
import dev.forcetower.events.domain.model.SimpleEvent
import org.junit.Assert.assertEquals
import org.junit.Test

class EventUnitTest {
    @Test
    fun `GIVEN an event THEN the fields are mapped correctly to simple event`() {
        val subject = EventMockFactory.create()
        val expected = SimpleEvent(subject.id, subject.title, subject.description, subject.image)
        assertEquals(expected, subject.toDomainSimple())
    }
    @Test
    fun `GIVEN an event THEN the fields are mapped correctly to complete event`() {
        val subject = EventMockFactory.create()
        val expected = CompleteEvent(
            subject.id,
            subject.title,
            subject.description,
            subject.image,
            subject.date,
            subject.latitude,
            subject.longitude,
            subject.price
        )
        assertEquals(expected, subject.toDomainComplete())
    }
}