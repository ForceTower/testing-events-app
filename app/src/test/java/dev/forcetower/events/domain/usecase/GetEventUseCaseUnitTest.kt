package dev.forcetower.events.domain.usecase

import dev.forcetower.events.data.mock.EventMockFactory
import dev.forcetower.events.domain.CommonUseCaseUnitTest
import dev.forcetower.events.domain.mappers.toDomainComplete
import io.mockk.every
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetEventUseCaseUnitTest : CommonUseCaseUnitTest() {
    private lateinit var useCase: GetEventUseCase

    @Before
    override fun before() {
        super.before()
        useCase = GetEventUseCase(repository)
    }

    @Test
    fun `GIVEN a event on database WHEN useCase is invoked THEN the event is returned`() = runTest {
        val subject = EventMockFactory.create().copy("fun!")
        every { eventDao.getById("fun!") } returns flowOf(subject)
        val expected = subject.toDomainComplete()
        val result = useCase("fun!").first()
        assertEquals(expected, result)
    }

    @Test
    fun `GIVEN no events on database WHEN useCase is invoked THEN nothing is emitted`() = runTest {
        every { eventDao.getById("not fun") } returns flowOf()
        val result = useCase("not fun").firstOrNull()
        assertNull(result)
    }
}