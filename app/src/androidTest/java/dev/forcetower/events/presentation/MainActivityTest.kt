package dev.forcetower.events.presentation

import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.os.Looper
import androidx.core.content.getSystemService
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.forcetower.events.R
import dev.forcetower.events.data.network.EventService
import dev.forcetower.events.data.network.FakeEventService
import dev.forcetower.events.tooling.atPositionOnView
import dev.forcetower.events.tooling.waitForId
import dev.forcetower.events.tooling.waitForItemCount
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import javax.inject.Inject

@HiltAndroidTest
class MainActivityTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var serviceGeneric: EventService
    private lateinit var service: FakeEventService

    private lateinit var app: HiltTestApplication

    @Before
    fun before() {
        hiltRule.inject()
        service = serviceGeneric as FakeEventService
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        app = instrumentation.targetContext.applicationContext as HiltTestApplication
        Intents.init()
    }

    @After
    fun after() {
        Intents.release()
    }

    @Test
    fun testEmptyFailedHomeScreenWithRefresh() {
        service.failEveryRequest(true)
        ActivityScenario.launch(MainActivity::class.java)

        onView(isRoot())
            .perform(waitForId(R.id.list_error_view, 1000))

        onView(withId(R.id.list_error_view))
            .check(matches(isDisplayed()))

        onView(withId(R.id.btn_try_again))
            .perform(click())

        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(R.string.events_list_failed_to_refresh)))

        onView(withId(R.id.btn_try_again))
            .perform(click())
    }

    @Test
    fun testListOfEventsOnHomeScreen() {
        ActivityScenario.launch(MainActivity::class.java)

        onView(isRoot())
            .perform(waitForId(R.id.recycler_events, 1000L))

        onView(withId(R.id.list_error_view))
            .check(matches(not(isDisplayed())))

        // The database returns elements sorted by id
        val elements = service.events.sortedBy { it.id }

        onView(withId(R.id.recycler_events))
            .check(matches(isDisplayed()))
            .perform(waitForItemCount(30, 1000L))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(10))
            .check(matches(atPositionOnView(10, withText(elements[10].title), R.id.title)))
    }

    @Test
    fun testNavigationToEventDetails() {
        ActivityScenario.launch(MainActivity::class.java)

        onView(isRoot())
            .perform(waitForId(R.id.recycler_events, 1000L))

        // The database returns elements sorted by id
        val subject = service.events.sortedBy { it.id }[10]

        service.failNextRequest()

        onView(withId(R.id.recycler_events))
            .check(matches(isDisplayed()))
            .perform(waitForItemCount(30, 1000L))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(10))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(10, click()))

        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(R.string.events_details_failed_to_refresh)))

        // Offline persistence will prevail
        onView(withId(R.id.text_details_title))
            .check(matches(isDisplayed()))
            .check(matches(withText(subject.title)))

        onView(withId(R.id.text_details_date))
            .check(matches(isDisplayed()))
            .check(matches(withText(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT).format(subject.date))))

        onView(withId(R.id.text_details_description))
            .check(matches(isDisplayed()))
            .check(matches(withText(subject.description)))
    }

    @Test
    fun testCheckInFlow() {
        ActivityScenario.launch(MainActivity::class.java)

        onView(isRoot())
            .perform(waitForId(R.id.recycler_events, 1000L))

        onView(withId(R.id.recycler_events))
            .check(matches(isDisplayed()))
            .perform(waitForItemCount(30, 1000L))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(10))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(10, click()))

        onView(withId(R.id.btn_check_in))
            .perform(click())

        onView(withId(R.id.btn_do_check_in))
            .perform(click())

        onView(withId(R.id.text_input_check_in_name))
            .check(matches(hasDescendant(withText(R.string.check_in_name_is_required))))

        onView(withId(R.id.text_input_check_in_email))
            .check(matches(hasDescendant(withText(R.string.check_in_email_is_required))))

        onView(withId(R.id.edit_text_check_in_name))
            .perform(typeText("lt"))
            .perform(ViewActions.closeSoftKeyboard())

        onView(withId(R.id.edit_text_check_in_email))
            .perform(typeText("invalid_email"))
            .perform(ViewActions.closeSoftKeyboard())

        onView(withId(R.id.btn_do_check_in))
            .perform(click())

        onView(withId(R.id.text_input_check_in_name))
            .check(matches(hasDescendant(withText(R.string.check_in_name_too_short))))

        onView(withId(R.id.text_input_check_in_email))
            .check(matches(hasDescendant(withText(R.string.check_in_email_invalid))))

        onView(withId(R.id.btn_do_check_in))
            .perform(click())

        onView(withId(R.id.edit_text_check_in_name))
            .perform(replaceText("valid name"))
            .perform(ViewActions.closeSoftKeyboard())

        onView(withId(R.id.btn_do_check_in))
            .perform(click())

        onView(withId(R.id.text_input_check_in_name))
            .check(matches(not(hasDescendant(withText(R.string.check_in_name_too_short)))))

        onView(withId(R.id.edit_text_check_in_email))
            .perform(replaceText("valid@email.com"))
            .perform(ViewActions.closeSoftKeyboard())

        service.failNextRequest()
        service.setNetworkDelay(500)

        onView(withId(R.id.btn_do_check_in))
            .perform(click())

        onView(withId(R.id.text_input_check_in_name))
            .check(matches(not(hasDescendant(withText(R.string.check_in_name_too_short)))))

        onView(withId(R.id.text_input_check_in_email))
            .check(matches(not(hasDescendant(withText(R.string.check_in_email_invalid)))))

        onView(withId(R.id.progress_check_in_loading))
            .check(matches(isDisplayed()))

        onView(isRoot())
            .perform(waitForId(R.id.error_check_in, 1000))

        onView(withId(R.id.btn_try_again))
            .perform(click())

        onView(withId(R.id.btn_do_check_in))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(isRoot())
            .perform(waitForId(R.id.container_check_in_success, 1000))
    }

    @Test
    fun testClickMaps() {
        ActivityScenario.launch(MainActivity::class.java)

        onView(isRoot())
            .perform(waitForId(R.id.recycler_events, 1000L))

        val subject = service.events.sortedBy { it.id }[10]

        onView(withId(R.id.recycler_events))
            .check(matches(isDisplayed()))
            .perform(waitForItemCount(30, 1000L))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(10))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(10, click()))

        onView(withId(R.id.item_map))
            .perform(click())

        intended(
            allOf(hasAction(Intent.ACTION_VIEW), hasData(Uri.parse("geo:${subject.latitude},${subject.longitude}")))
        )
    }

    @Test
    fun testShare() {
        ActivityScenario.launch(MainActivity::class.java)

        onView(isRoot())
            .perform(waitForId(R.id.recycler_events, 1000L))

        val subject = service.events.sortedBy { it.id }[10]

        onView(withId(R.id.recycler_events))
            .check(matches(isDisplayed()))
            .perform(waitForItemCount(30, 1000L))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(10))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(10, click()))

        onView(withId(R.id.item_share))
            .perform(click())

        Looper.prepare()
        val manager = InstrumentationRegistry.getInstrumentation().targetContext.getSystemService<ClipboardManager>()
        val item = manager?.primaryClip?.getItemAt(0)?.text
        val expected = """
            ${subject.title}
            ${subject.date.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT))}
        """.trimIndent()

        // data copied to the clipboard
        assertEquals(expected, item)

        onView(withId(R.id.btn_complete))
            .check(matches(isDisplayed()))
            .perform(click())
    }
}
