package dev.forcetower.events.domain.usecase

import dev.forcetower.events.data.model.dto.CheckInRequest
import dev.forcetower.events.domain.CommonUseCaseUnitTest
import io.mockk.coVerify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CheckInUseCaseUnitTest : CommonUseCaseUnitTest() {
    private lateinit var useCase: CheckInUseCase

    @Before
    override fun before() {
        super.before()
        useCase = CheckInUseCase(repository)
    }

    @Test
    fun `GIVEN valid data WHEN use case is invoked THEN service is called with correct info`() = runTest {
        useCase(VALID_NAME, EMAIL, EVENT_ID)
        coVerify(exactly = 1) { service.checkIn(CheckInRequest(EVENT_ID, VALID_NAME, EMAIL)) }
    }

    @Test(expected = IllegalStateException::class)
    fun `GIVEN invalid data WHEN use case is invoked THEN service throws an error`() = runTest {
        useCase(INVALID_NAME, EMAIL, EVENT_ID)
        coVerify(exactly = 1) { service.checkIn(CheckInRequest(EVENT_ID, INVALID_NAME, EMAIL)) }
    }

    @Test(expected = IllegalArgumentException::class)
    fun `GIVEN invalid email WHEN use case is invoked THEN repository throws an error`() = runTest {
        useCase(VALID_NAME, INVALID_EMAIL, EVENT_ID)
        coVerify(exactly = 0) { service.checkIn(CheckInRequest(any(), any(), any())) }
    }
}
