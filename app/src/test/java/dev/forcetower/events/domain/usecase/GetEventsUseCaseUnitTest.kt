package dev.forcetower.events.domain.usecase

import dev.forcetower.events.data.mock.EventMockFactory
import dev.forcetower.events.domain.CommonUseCaseUnitTest
import dev.forcetower.events.domain.mappers.toDomainSimple
import dev.forcetower.events.domain.model.SimpleEvent
import io.mockk.coEvery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetEventsUseCaseUnitTest : CommonUseCaseUnitTest() {
    private lateinit var useCase: GetEventsUseCase

    @Before
    override fun before() {
        super.before()
        useCase = GetEventsUseCase(repository)
    }

    @Test
    fun `GIVEN events on database WHEN useCase is invoked THEN events are emitted`() = runTest {
        val subject = EventMockFactory.createList()
        coEvery { eventDao.list() } returns flowOf(subject)

        val expected = subject.map { it.toDomainSimple() }
        val contents = useCase().first()
        assertEquals(expected, contents)
    }

    @Test
    fun `GIVEN there's no events on database WHEN useCase is invoked THEN empty list is emitted`() = runTest {
        val empty = emptyList<SimpleEvent>()
        coEvery { eventDao.list() } returns flowOf(emptyList())
        val contents = useCase().first()
        assertEquals(empty, contents)
    }
}