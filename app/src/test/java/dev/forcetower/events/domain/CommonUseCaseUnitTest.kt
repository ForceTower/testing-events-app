package dev.forcetower.events.domain

import dev.forcetower.events.data.local.EventDB
import dev.forcetower.events.data.local.dao.EventDao
import dev.forcetower.events.data.model.dto.CheckInRequest
import dev.forcetower.events.data.network.EventService
import dev.forcetower.events.data.repository.EventRepository
import dev.forcetower.events.data.repository.EventRepositoryImpl
import dev.forcetower.events.domain.validator.Validator
import dev.forcetower.events.tooling.BaseUnitTest
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before

@ExperimentalCoroutinesApi
abstract class CommonUseCaseUnitTest : BaseUnitTest() {
    protected lateinit var service: EventService
    protected lateinit var validator: Validator<String>
    protected lateinit var repository: EventRepository
    protected lateinit var database: EventDB
    protected lateinit var eventDao: EventDao

    @Before
    open fun before() {
        service = mockk(relaxed = true)
        validator = mockk(relaxed = true)
        database = mockk(relaxed = true)
        eventDao = mockk(relaxed = true)

        repository = EventRepositoryImpl(database, service, validator)

        coEvery { database.events } returns eventDao
        coEvery { service.checkIn(CheckInRequest(EVENT_ID, VALID_NAME, EMAIL)) } returns Unit
        coEvery { service.checkIn(CheckInRequest(EVENT_ID, INVALID_NAME, EMAIL)) } throws IllegalStateException(":(")
        every { validator.isValid(EMAIL) } returns true
        every { validator.isValid(INVALID_EMAIL) } returns false
    }

    companion object {
        const val VALID_NAME = "valid"
        const val INVALID_NAME = "invalid"

        const val EMAIL = "hey@soul.com"
        const val INVALID_EMAIL = "not_an_email"
        const val EVENT_ID = "ID :)"
    }
}
