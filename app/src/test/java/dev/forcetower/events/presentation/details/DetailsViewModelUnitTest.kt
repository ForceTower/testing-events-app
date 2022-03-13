package dev.forcetower.events.presentation.details

import androidx.lifecycle.Observer
import dev.forcetower.events.R
import dev.forcetower.events.domain.model.CompleteEvent
import dev.forcetower.events.domain.model.CompleteEventMockFactory
import dev.forcetower.events.domain.usecase.GetEventUseCase
import dev.forcetower.events.domain.usecase.UpdateEventUseCase
import dev.forcetower.events.tooling.BaseUnitTest
import dev.forcetower.events.tooling.lifecycle.EventObserver
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@ExperimentalCoroutinesApi
class DetailsViewModelUnitTest : BaseUnitTest() {
    private lateinit var getEventUseCase: GetEventUseCase
    private lateinit var refreshEventUseCase: UpdateEventUseCase

    private lateinit var viewModel: DetailsViewModel

    private lateinit var checkInHandle: (String) -> Unit
    private lateinit var refreshFailedHandle: (Unit) -> Unit
    private lateinit var shareInfoHandle: (String) -> Unit
    private lateinit var openMapHandle: (Pair<Double, Double>) -> Unit

    private lateinit var eventObserver: Observer<CompleteEvent>
    private lateinit var checkInObserver: EventObserver<String>
    private lateinit var refreshFailedObserver: EventObserver<Unit>
    private lateinit var shareInfoObserver: EventObserver<String>
    private lateinit var openMapObserver: EventObserver<Pair<Double, Double>>

    private lateinit var item: CompleteEvent

    @Before
    fun before() {
        item = CompleteEventMockFactory.create().copy(id = "event")
        getEventUseCase = mockk(relaxed = true)
        refreshEventUseCase = mockk(relaxed = true)

        checkInHandle = mockk(relaxed = true)
        refreshFailedHandle = mockk(relaxed = true)
        shareInfoHandle = mockk(relaxed = true)
        openMapHandle = mockk(relaxed = true)

        eventObserver = mockk(relaxed = true)
        checkInObserver = EventObserver(checkInHandle)
        refreshFailedObserver = EventObserver(refreshFailedHandle)
        shareInfoObserver = EventObserver(shareInfoHandle)
        openMapObserver = EventObserver(openMapHandle)

        coEvery { getEventUseCase("event") } returns flowOf(item)
        viewModel = DetailsViewModel("event", getEventUseCase, refreshEventUseCase)

        viewModel.event.observeForever(eventObserver)
        viewModel.onCheckIn.observeForever(checkInObserver)
        viewModel.onRefreshFailed.observeForever(refreshFailedObserver)
        viewModel.onShareInfo.observeForever(shareInfoObserver)
        viewModel.onOpenMap.observeForever(openMapObserver)
    }

    @After
    fun after() {
        viewModel.event.removeObserver(eventObserver)
        viewModel.onCheckIn.removeObserver(checkInObserver)
        viewModel.onRefreshFailed.removeObserver(refreshFailedObserver)
        viewModel.onShareInfo.removeObserver(shareInfoObserver)
        viewModel.onOpenMap.removeObserver(openMapObserver)
    }

    @Test
    fun `GIVEN the initial state WHEN the view model is created THEN getEventUseCase is invoked`() {
        verify(exactly = 1) { getEventUseCase("event") }
    }

    @Test
    fun `GIVEN successful values WHEN the view model is created THEN a event is emitted`() {
        verify { eventObserver.onChanged(item) }
    }

    @Test
    fun `GIVEN successful values WHEN refresh is called THEN onRefreshFailed event is not emitted`() {
        viewModel.refresh()
        verify(exactly = 0) { refreshFailedHandle(Unit) }
    }

    @Test
    fun `GIVEN error values WHEN refresh is called THEN onRefreshFailed event is emitted`() {
        coEvery { refreshEventUseCase("event") } throws IllegalStateException("Something bad")
        viewModel.refresh()
        verify(exactly = 1) { refreshFailedHandle(Unit) }
    }

    @Test
    fun `GIVEN an undefined value WHEN calling onMenuItemClicked THEN nothing happens`() {
        viewModel.onMenuItemClicked(R.id.item_share, null)
        viewModel.onMenuItemClicked(R.id.item_map, null)
        viewModel.onMenuItemClicked(0, null)

        verify(exactly = 0) {
            shareInfoHandle(any())
            openMapHandle(any())
        }
    }

    @Test
    fun `GIVEN an event with a share action WHEN calling onMenuItemClicked THEN share is emitted`() {
        viewModel.onMenuItemClicked(R.id.item_share, item)
        val expectedStr = """
            ${item.title}
            ${item.date.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT))}
        """.trimIndent()
        verify(exactly = 0) { openMapHandle(any()) }
        verify(exactly = 1) { shareInfoHandle(expectedStr) }
    }

    @Test
    fun `GIVEN an event with an open map action WHEN calling onMenuItemClicked THEN map is emitted`() {
        viewModel.onMenuItemClicked(R.id.item_map, item)
        val expectedObj = item.latitude to item.longitude
        verify(exactly = 0) { shareInfoHandle(any()) }
        verify(exactly = 1) { openMapHandle(expectedObj) }
    }

    @Test
    fun `GIVEN an event WHEN checkInEvent is invoked THEN onCheckIn event is emitted`() {
        viewModel.checkInEvent(item)
        verify(exactly = 1) { checkInHandle(item.id) }
    }

    @Test
    fun `GIVEN an undefined event WHEN checkInEvent is invoked THEN onCheckIn event not is emitted`() {
        viewModel.checkInEvent(null)
        verify(exactly = 0) { checkInHandle(any()) }
    }
}
