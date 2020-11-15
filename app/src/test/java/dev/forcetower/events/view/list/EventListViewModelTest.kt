package dev.forcetower.events.view.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import dev.forcetower.events.core.model.Event
import dev.forcetower.events.core.model.EventFakeDataFactory
import dev.forcetower.events.core.source.local.EventDB
import dev.forcetower.events.core.source.remote.EventService
import dev.forcetower.events.core.source.repository.EventRepository
import dev.forcetower.toolkit.lifecycle.EventObserver
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.every
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
class EventListViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val testDispatcher = TestCoroutineDispatcher()

    private val database = mockk<EventDB>()
    private val service = mockk<EventService>()
    private val eventsLiveDataObserver = mockk<Observer<List<Event>>>(relaxed = true)
    private val eventsLoadingLiveDataObserver = mockk<Observer<Boolean>>(relaxed = true)
    private val eventsErrorLiveDataObserver = mockk<EventObserver<Int?>>(relaxed = true)

    private lateinit var viewModel: EventListViewModel
    private lateinit var repository: EventRepository

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = EventRepository(database, service)
        viewModel = EventListViewModel(repository)
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `load events from database correctly`() = runBlockingTest {
        every { database.events().getEvents() } returns flowOf(EventFakeDataFactory.EVENT_LIST)
        viewModel.getEvents().observeForever(eventsLiveDataObserver)
        verify { eventsLiveDataObserver.onChanged(EventFakeDataFactory.EVENT_LIST) }
    }

    @Test
    fun `load events from network correctly`() = runBlockingTest {
        coEvery { service.events() } returns EventFakeDataFactory.EVENT_LIST
        coEvery { database.events().insertOrUpdate(EventFakeDataFactory.EVENT_LIST) } returns Unit

        viewModel.loading.observeForever(eventsLoadingLiveDataObserver)
        viewModel.onUpdateError.observeForever(eventsErrorLiveDataObserver)

        verify { eventsLoadingLiveDataObserver.onChanged(false) }

        viewModel.updateList(true)

        coVerifyOrder {
            eventsLoadingLiveDataObserver.onChanged(true)
            repository.updateEvents()
            eventsLoadingLiveDataObserver.onChanged(false)
        }
        verify(exactly = 0) { eventsErrorLiveDataObserver.onChanged(any()) }
    }

    @Test
    fun `load events from network with error`() = runBlockingTest {
        coEvery { service.events() } throws IOException("failed!")
        coEvery { database.events().insertOrUpdate(EventFakeDataFactory.EVENT_LIST) } returns Unit

        viewModel.loading.observeForever(eventsLoadingLiveDataObserver)
        viewModel.onUpdateError.observeForever(eventsErrorLiveDataObserver)

        viewModel.updateList(true)
        verify(exactly = 1) { eventsErrorLiveDataObserver.onChanged(any()) }
    }
}
