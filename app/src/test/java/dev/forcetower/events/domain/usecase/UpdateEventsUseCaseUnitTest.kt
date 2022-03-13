package dev.forcetower.events.domain.usecase

import dev.forcetower.events.data.model.Event
import dev.forcetower.events.data.mock.EventMockFactory
import dev.forcetower.events.domain.CommonUseCaseUnitTest
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class UpdateEventsUseCaseUnitTest : CommonUseCaseUnitTest() {
    private lateinit var useCase: UpdateEventsUseCase

    @Before
    override fun before() {
        super.before()
        useCase = UpdateEventsUseCase(repository)
    }

    @Test
    fun `GIVEN a successful response WHEN useCase is invoked THEN items are inserted onto database`() = runTest {
        val subjects = EventMockFactory.createList(20)
        coEvery { service.events() } returns subjects
        coEvery { eventDao.upsert(subjects) } returns Unit

        useCase()

        coVerify(exactly = 1) { eventDao.upsert(subjects) }
    }

    @Test(expected = IllegalStateException::class)
    fun `GIVEN a fail response WHEN useCase is invoked THEN items are not inserted onto database`() = runTest {
        coEvery { service.events() } throws IllegalStateException(":(")
        useCase()
        coVerify(exactly = 0) { database.events.upsert(any<List<Event>>()) }
    }
}