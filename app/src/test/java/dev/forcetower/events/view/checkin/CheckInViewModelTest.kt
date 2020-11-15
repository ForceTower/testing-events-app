package dev.forcetower.events.view.checkin

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import dev.forcetower.events.R
import dev.forcetower.events.core.model.CheckInData
import dev.forcetower.events.core.source.local.EventDB
import dev.forcetower.events.core.source.remote.EventService
import dev.forcetower.events.core.source.repository.EventRepository
import dev.forcetower.toolkit.lifecycle.EventObserver
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException
import java.util.regex.Pattern

@ExperimentalCoroutinesApi
class CheckInViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val testDispatcher = TestCoroutineDispatcher()

    private val service = mockk<EventService>()
    private val database = mockk<EventDB>()

    private val loadingLiveDataObserver = mockk<Observer<Boolean>>(relaxed = true)
    private val nameErrorLiveDataObserver = mockk<Observer<Int?>>(relaxed = true)
    private val emailErrorLiveDataObserver = mockk<Observer<Int?>>(relaxed = true)
    private val onRegisteredEventLiveDataObserver = mockk<EventObserver<Unit>>(relaxed = true)

    private lateinit var viewModel: CheckInViewModel
    private lateinit var repository: EventRepository

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = EventRepository(database, service)
        viewModel = CheckInViewModel(repository, TestingEmailValidator())
        viewModel.setEventId("1")
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `empty data check in`() {
        viewModel.nameError.observeForever(nameErrorLiveDataObserver)
        viewModel.emailError.observeForever(emailErrorLiveDataObserver)
        viewModel.loading.observeForever(loadingLiveDataObserver)
        viewModel.onRegistered.observeForever(onRegisteredEventLiveDataObserver)

        viewModel.name.value = ""
        viewModel.email.value = ""

        viewModel.checkIn()

        verifyAll {
            nameErrorLiveDataObserver.onChanged(R.string.name_is_required)
            emailErrorLiveDataObserver.onChanged(R.string.email_is_required)
            loadingLiveDataObserver.onChanged(false)
        }

        // never called
        verify(exactly = 0) { onRegisteredEventLiveDataObserver.onChanged(any()) }
    }

    @Test
    fun `invalid data check in`() {
        viewModel.nameError.observeForever(nameErrorLiveDataObserver)
        viewModel.emailError.observeForever(emailErrorLiveDataObserver)
        viewModel.loading.observeForever(loadingLiveDataObserver)
        viewModel.onRegistered.observeForever(onRegisteredEventLiveDataObserver)

        viewModel.name.value = "sm"
        viewModel.email.value = "not_a_email"

        viewModel.checkIn()

        verifyAll {
            nameErrorLiveDataObserver.onChanged(R.string.name_too_short)
            emailErrorLiveDataObserver.onChanged(R.string.email_invalid)
            loadingLiveDataObserver.onChanged(false)
        }

        // never called
        verify(exactly = 0) { onRegisteredEventLiveDataObserver.onChanged(any()) }
    }

    @Test
    fun `valid data check in`() = runBlockingTest {
        coEvery { service.checkIn(CheckInData("1", "John Cena", "email@email.com")) } returns Unit
        viewModel.nameError.observeForever(nameErrorLiveDataObserver)
        viewModel.emailError.observeForever(emailErrorLiveDataObserver)
        viewModel.loading.observeForever(loadingLiveDataObserver)
        viewModel.onRegistered.observeForever(onRegisteredEventLiveDataObserver)

        viewModel.name.value = "John Cena"
        viewModel.email.value = "email@email.com"

        viewModel.checkIn()

        coVerifyOrder {
            loadingLiveDataObserver.onChanged(false)
            nameErrorLiveDataObserver.onChanged(null)
            emailErrorLiveDataObserver.onChanged(null)
            loadingLiveDataObserver.onChanged(true)
            repository.checkIn("1", "John Cena", "email@email.com")
            onRegisteredEventLiveDataObserver.onChanged(any())
            loadingLiveDataObserver.onChanged(false)
        }
    }

    @Test
    fun `network error on check in`() = runBlockingTest {
        coEvery { service.checkIn(CheckInData("1", "John Cena", "email@email.com")) } throws IOException("ops!")
        viewModel.nameError.observeForever(nameErrorLiveDataObserver)
        viewModel.emailError.observeForever(emailErrorLiveDataObserver)
        viewModel.loading.observeForever(loadingLiveDataObserver)
        viewModel.onRegistered.observeForever(onRegisteredEventLiveDataObserver)

        viewModel.name.value = "John Cena"
        viewModel.email.value = "email@email.com"

        viewModel.checkIn()

        coVerifyOrder {
            loadingLiveDataObserver.onChanged(false)
            nameErrorLiveDataObserver.onChanged(null)
            emailErrorLiveDataObserver.onChanged(null)
            loadingLiveDataObserver.onChanged(true)
            repository.checkIn("1", "John Cena", "email@email.com")
            loadingLiveDataObserver.onChanged(false)
        }

        verify(exactly = 0) { onRegisteredEventLiveDataObserver.onChanged(any()) }
    }
}