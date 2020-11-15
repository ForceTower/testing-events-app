package dev.forcetower.events.view.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import dev.forcetower.events.core.model.Event
import dev.forcetower.events.core.model.EventFakeDataFactory
import dev.forcetower.events.core.source.local.EventDB
import dev.forcetower.events.core.source.remote.EventService
import dev.forcetower.events.core.source.repository.EventRepository
import dev.forcetower.toolkit.lifecycle.EventObserver
import dev.forcetower.toolkit.lifecycle.LiveEvent
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class EventDetailsViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val testDispatcher = TestCoroutineDispatcher()

    private val database = mockk<EventDB>()
    private val service = mockk<EventService>()
    private val eventLiveDataObserver = mockk<Observer<Event>>(relaxed = true)
    private val checkInEventLiveDataObserver = mockk<EventObserver<Event>>(relaxed = true)

    private lateinit var viewModel: EventDetailsViewModel
    private lateinit var repository: EventRepository

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = EventRepository(database, service)
        viewModel = EventDetailsViewModel(repository)
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `load event from database correctly`() = runBlockingTest {
        coEvery { database.events().getEventById("1") } returns flowOf(EventFakeDataFactory.EVENT_DEFAULT)
        viewModel.getEvent("1").observeForever(eventLiveDataObserver)

        verify { eventLiveDataObserver.onChanged(EventFakeDataFactory.EVENT_DEFAULT) }
    }

    @Test
    fun `load event from network correctly`() = runBlockingTest {
        coEvery { service.event("1") } returns EventFakeDataFactory.EVENT_DEFAULT
        coEvery { database.events().insertOrUpdate(EventFakeDataFactory.EVENT_DEFAULT) } returns Unit

        viewModel.updateEvent("1")

        coVerifyOrder {
            repository.updateEvent("1")
            service.event("1")
            database.events().insert(EventFakeDataFactory.EVENT_DEFAULT)
        }
    }

    @Test
    fun `load event from network incorrectly`() = runBlockingTest {
        coEvery { service.event("1") } throws IOException("Nice!")
        coEvery { database.events().insertOrUpdate(EventFakeDataFactory.EVENT_DEFAULT) } returns Unit

        // no error is thrown
        viewModel.updateEvent("1")

        coVerifyOrder {
            repository.updateEvent("1")
            service.event("1")
        }

        // never inserted
        coVerify(exactly = 0) { database.events().insert(EventFakeDataFactory.EVENT_DEFAULT) }
    }

    @Test
    fun `fire up check in event`() = runBlockingTest {
        viewModel.onCheckIn.observeForever(checkInEventLiveDataObserver)

        val event = EventFakeDataFactory.makeEvent()
        viewModel.checkInEvent(event)
        verify { checkInEventLiveDataObserver.onChanged(LiveEvent(event)) }
    }
}
