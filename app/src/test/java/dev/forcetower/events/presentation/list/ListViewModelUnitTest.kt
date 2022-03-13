package dev.forcetower.events.presentation.list

import androidx.lifecycle.Observer
import dev.forcetower.events.domain.model.EventSimpleMockFactory
import dev.forcetower.events.domain.model.SimpleEvent
import dev.forcetower.events.domain.usecase.GetEventsUseCase
import dev.forcetower.events.domain.usecase.UpdateEventsUseCase
import dev.forcetower.events.tooling.BaseUnitTest
import dev.forcetower.events.tooling.lifecycle.EventObserver
import io.mockk.Ordering
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ListViewModelUnitTest : BaseUnitTest() {
    private lateinit var getEventsUseCase: GetEventsUseCase
    private lateinit var refreshUseCase: UpdateEventsUseCase

    private lateinit var viewModel: ListViewModel
    private lateinit var initialList: List<SimpleEvent>

    private lateinit var eventsObserver: Observer<List<SimpleEvent>>
    private lateinit var eventSelectedHandle: (String) -> Unit
    private lateinit var errorRefreshHandle: (Unit) -> Unit
    private lateinit var refreshingObserver: Observer<Boolean>
    private lateinit var showErrorObserver: Observer<Boolean>

    private lateinit var eventSelectedObserver: EventObserver<String>
    private lateinit var errorRefreshObserver: EventObserver<Unit>

    @Before
    fun before() {
        getEventsUseCase = mockk(relaxed = true)
        refreshUseCase = mockk(relaxed = true)

        eventsObserver = mockk(relaxed = true)
        eventSelectedHandle = mockk(relaxed = true)
        errorRefreshHandle = mockk(relaxed = true)
        refreshingObserver = mockk(relaxed = true)
        showErrorObserver = mockk(relaxed = true)

        initialList = EventSimpleMockFactory.createList()
        coEvery { refreshUseCase() } returns Unit
        coEvery { getEventsUseCase() } returns flowOf(initialList)

        viewModel = ListViewModel(getEventsUseCase, refreshUseCase)

        eventSelectedObserver = EventObserver(eventSelectedHandle)
        errorRefreshObserver = EventObserver(errorRefreshHandle)

        viewModel.events.observeForever(eventsObserver)
        viewModel.onEventSelected.observeForever(eventSelectedObserver)
        viewModel.onRefreshFailed.observeForever(errorRefreshObserver)
        viewModel.refreshing.observeForever(refreshingObserver)
        viewModel.showError.observeForever(showErrorObserver)
    }

    @After
    fun after() {
        viewModel.events.removeObserver(eventsObserver)
        viewModel.onEventSelected.removeObserver(eventSelectedObserver)
        viewModel.onRefreshFailed.removeObserver(errorRefreshObserver)
        viewModel.refreshing.removeObserver(refreshingObserver)
        viewModel.showError.removeObserver(showErrorObserver)
    }

    @Test
    fun `GIVEN successful values WHEN the view model is created THEN getEventsUseCase is invoked`() {
        coVerify(exactly = 1) { getEventsUseCase() }
    }

    @Test
    fun `GIVEN successful values WHEN refresh is invoked THEN onRefreshFailed event is not emitted `() {
        viewModel.refresh()
        coVerify(exactly = 0) { errorRefreshHandle(Unit) }
    }

    @Test
    fun `GIVEN successful values THEN events are emitted`() {
        verify { eventsObserver.onChanged(initialList) }
    }

    @Test
    fun `GIVEN the initial state WHEN an event is selected THEN onEventSelected event is emitted`() {
        val subject = EventSimpleMockFactory.create()
        viewModel.onEventClicked(subject)
        verify(exactly = 1) { eventSelectedHandle(subject.id) }
    }

    @Test
    fun `GIVEN a null event WHEN it is selected THEN onEventSelected event is not emitted`() {
        viewModel.onEventClicked(null)
        verify(exactly = 0) { eventSelectedHandle(any()) }
    }

    @Test
    fun `GIVEN an error WHEN refresh is invoked THEN onRefreshFailed event is emitted`() {
        coEvery { refreshUseCase() } throws IllegalStateException("Why? :(")

        viewModel.refresh()
        verify(exactly = 1) { errorRefreshHandle(Unit) }
    }

    @Test
    fun `GIVEN a list of events WHEN it is not refreshing THEN show error should be false`() {
        val subject = EventSimpleMockFactory.createList()
        coEvery { getEventsUseCase() } returns flowOf(subject)
        viewModel = ListViewModel(getEventsUseCase, refreshUseCase)
        viewModel.showError.observeForever(showErrorObserver)

        verify(exactly = 0) { showErrorObserver.onChanged(true) }
    }

    @Test
    fun `GIVEN an empty list of events WHEN it is not refreshing THEN show error should be true`() {
        coEvery { getEventsUseCase() } returns flowOf(emptyList())
        viewModel = ListViewModel(getEventsUseCase, refreshUseCase)
        viewModel.showError.observeForever(showErrorObserver)

        verify(ordering = Ordering.SEQUENCE) {
            showErrorObserver.onChanged(false) // initial loading
            showErrorObserver.onChanged(true)
        }
    }
}
