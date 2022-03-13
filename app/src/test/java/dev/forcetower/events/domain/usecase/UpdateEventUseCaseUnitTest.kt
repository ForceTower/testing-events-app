package dev.forcetower.events.domain.usecase

import dev.forcetower.events.data.model.Event
import dev.forcetower.events.data.model.EventMockFactory
import dev.forcetower.events.domain.CommonUseCaseUnitTest
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class UpdateEventUseCaseUnitTest : CommonUseCaseUnitTest() {
    private lateinit var useCase: UpdateEventUseCase

    @Before
    override fun before() {
        super.before()
        useCase = UpdateEventUseCase(repository)
    }

    @Test
    fun `GIVEN a successful response WHEN useCase is invoked THEN items are inserted onto database`() = runTest {
        val subject = EventMockFactory.create().copy("hey")
        coEvery { service.event("hey") } returns subject
        coEvery { eventDao.upsert(subject) } returns Unit

        useCase("hey")

        coVerify(exactly = 1) { eventDao.upsert(subject) }
    }

    @Test(expected = IllegalStateException::class)
    fun `GIVEN a fail response WHEN useCase is invoked THEN items are not inserted onto database`() = runTest {
        coEvery { service.event("there") } throws IllegalStateException(":(")
        useCase("there")
        coVerify(exactly = 0) { database.events.upsert(any<Event>()) }
    }
}