package dev.forcetower.events.tooling

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule

@ExperimentalCoroutinesApi
abstract class BaseUnitTest {
    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()
    @get:Rule
    open val coroutineRule = CoroutinesMainTestRule()
}