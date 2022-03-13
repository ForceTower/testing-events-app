package dev.forcetower.events.presentation.checkin

import androidx.lifecycle.Observer
import dev.forcetower.events.R
import dev.forcetower.events.domain.usecase.CheckInUseCase
import dev.forcetower.events.domain.validator.Validator
import dev.forcetower.events.tooling.BaseUnitTest
import dev.forcetower.events.tooling.CoroutinesMainTestRule
import dev.forcetower.events.tooling.lifecycle.EventObserver
import io.mockk.Ordering
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

private const val NAME_SUCCESS = "success"
private const val NAME_ERROR = "fail"
private const val EMAIL = "user@user.com"
private const val EVENT_ID = "ski-ba-bop-ba-dop-bop"

@ExperimentalCoroutinesApi
class CheckInViewModelUnitTest : BaseUnitTest() {
    private lateinit var checkInUseCase: CheckInUseCase
    private val validator = object : Validator<String> {
        override fun isValid(value: String) = value.contains("@")
    }

    private lateinit var viewModel: CheckInViewModel

    private lateinit var completedHandle: (Unit) -> Unit

    private lateinit var nameErrorObserver: Observer<Int?>
    private lateinit var emailErrorObserver: Observer<Int?>
    private lateinit var loadingObserver: Observer<Boolean>
    private lateinit var completedObserver: Observer<Boolean>
    private lateinit var errorObserver: Observer<Boolean>
    private lateinit var fillingObserver: Observer<Boolean>
    private lateinit var onCompletedObserver: EventObserver<Unit>

    private val scheduler = TestCoroutineScheduler()
    override val coroutineRule: CoroutinesMainTestRule
        get() = CoroutinesMainTestRule(StandardTestDispatcher(scheduler))

    @Before
    fun before() {
        checkInUseCase = mockk(relaxed = true)

        completedHandle = mockk(relaxed = true)
        nameErrorObserver = mockk(relaxed = true)
        emailErrorObserver = mockk(relaxed = true)
        loadingObserver = mockk(relaxed = true)
        completedObserver = mockk(relaxed = true)
        errorObserver = mockk(relaxed = true)
        fillingObserver = mockk(relaxed = true)
        onCompletedObserver = EventObserver(completedHandle)

        coEvery { checkInUseCase(NAME_SUCCESS, EMAIL, EVENT_ID) } returns Unit
        coEvery { checkInUseCase(NAME_ERROR, EMAIL, EVENT_ID) } throws IllegalStateException(":(")

        viewModel = CheckInViewModel(EVENT_ID, checkInUseCase, validator)

        viewModel.nameError.observeForever(nameErrorObserver)
        viewModel.emailError.observeForever(emailErrorObserver)
        viewModel.loading.observeForever(loadingObserver)
        viewModel.completed.observeForever(completedObserver)
        viewModel.error.observeForever(errorObserver)
        viewModel.filling.observeForever(fillingObserver)
        viewModel.onCompleted.observeForever(onCompletedObserver)
    }

    @Test
    fun `GIVEN the initial state WHEN the viewModel is created THEN the form is on Filling State`() {
        verify { fillingObserver.onChanged(true) }
        verify(exactly = 0) { completedObserver.onChanged(true) }
        verify(exactly = 0) { errorObserver.onChanged(true) }
        verify(exactly = 0) { loadingObserver.onChanged(true) }
    }

    @Test
    fun `GIVEN an invalid name WHEN checkIn is called THEN nameError is changed`() {
        viewModel.name.value = ""
        viewModel.checkIn()
        verify { nameErrorObserver.onChanged(R.string.check_in_name_is_required) }
        coVerify(exactly = 0) { checkInUseCase(any(), any(), any()) }

        viewModel.name.value = "ab   "
        viewModel.checkIn()
        verify { nameErrorObserver.onChanged(R.string.check_in_name_too_short) }
        coVerify(exactly = 0) { checkInUseCase(any(), any(), any()) }
    }

    @Test
    fun `GIVEN an invalid email WHEN checkIn is called THEN emailError is changed`() {
        viewModel.email.value = ""
        viewModel.checkIn()
        verify { emailErrorObserver.onChanged(R.string.check_in_email_is_required) }
        coVerify(exactly = 0) { checkInUseCase(any(), any(), any()) }

        viewModel.email.value = "abcd"
        viewModel.checkIn()
        verify { emailErrorObserver.onChanged(R.string.check_in_email_invalid) }
        coVerify(exactly = 0) { checkInUseCase(any(), any(), any()) }
    }

    @Test
    fun `GIVEN valid info WHEN checkIn is called THEN success states are emitted`() {
        viewModel.name.value = NAME_SUCCESS
        viewModel.email.value = EMAIL
        viewModel.checkIn()
        scheduler.advanceUntilIdle()
        verify(exactly = 1) { nameErrorObserver.onChanged(null) }
        verify(exactly = 1) { emailErrorObserver.onChanged(null) }
        coVerify(exactly = 1) { checkInUseCase(NAME_SUCCESS, EMAIL, EVENT_ID) }

        verify(ordering = Ordering.ORDERED) {
            loadingObserver.onChanged(true)
            completedObserver.onChanged(true)
            completedHandle(Unit)
        }

        verify(exactly = 0) { errorObserver.onChanged(true) }

        assertTrue(viewModel.completed.value ?: false)
    }

    @Test
    fun `GIVEN valid info and an unknown error WHEN checkIn is called THEN error states are emitted`() {
        viewModel.name.value = NAME_ERROR
        viewModel.email.value = EMAIL
        viewModel.checkIn()
        scheduler.advanceUntilIdle()
        verify(exactly = 1) { nameErrorObserver.onChanged(null) }
        verify(exactly = 1) { emailErrorObserver.onChanged(null) }
        coVerify(exactly = 1) { checkInUseCase(NAME_ERROR, EMAIL, EVENT_ID) }

        verify(ordering = Ordering.ORDERED) {
            loadingObserver.onChanged(true)
            errorObserver.onChanged(true)
        }

        verify(exactly = 0) { completedObserver.onChanged(true) }
        assertTrue(viewModel.error.value ?: false)
    }

    @Test
    fun `GIVEN an error happened WHEN checkInAgain is called THEN the state resets to filling`() {
        viewModel.name.value = NAME_ERROR
        viewModel.email.value = EMAIL
        viewModel.checkIn()
        scheduler.advanceUntilIdle()
        verify(exactly = 1) { nameErrorObserver.onChanged(null) }
        verify(exactly = 1) { emailErrorObserver.onChanged(null) }
        coVerify(exactly = 1) { checkInUseCase(NAME_ERROR, EMAIL, EVENT_ID) }

        // User is at error state
        assertTrue(viewModel.error.value ?: false)

        viewModel.checkInAgain()

        verify {
            errorObserver.onChanged(false)
            fillingObserver.onChanged(true)
        }

        // User is at filling state
        assertTrue(viewModel.filling.value ?: false)
    }
}
