package dev.forcetower.events.tooling

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.util.HumanReadables
import androidx.test.espresso.util.TreeIterables
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher
import java.util.concurrent.TimeoutException

fun waitForId(
    viewId: Int,
    millis: Long
): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return isRoot()
        }

        override fun getDescription(): String {
            return "wait for a specific view with id <$viewId> during $millis millis."
        }

        override fun perform(uiController: UiController, view: View) {
            uiController.loopMainThreadUntilIdle()
            val startTime = System.currentTimeMillis()
            val endTime = startTime + millis
            val viewMatcher: Matcher<View> = withId(viewId)

            do {
                for (child in TreeIterables.breadthFirstViewTraversal(view)) {
                    if (viewMatcher.matches(child) && child.visibility == View.VISIBLE) {
                        return
                    }
                }
                uiController.loopMainThreadForAtLeast(50)
            } while (System.currentTimeMillis() < endTime)

            throw PerformException.Builder()
                .withActionDescription(this.description)
                .withViewDescription(HumanReadables.describe(view))
                .withCause(TimeoutException())
                .build()
        }
    }
}

fun waitForItemCount(
    itemCount: Int,
    millis: Long
): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return Matchers.allOf(
                ViewMatchers.isAssignableFrom(
                    RecyclerView::class.java
                ),
                ViewMatchers.isDisplayed()
            )
        }

        override fun getDescription(): String {
            return "wait for a recyclerview have item count <$itemCount> during $millis millis."
        }

        override fun perform(uiController: UiController, view: View) {
            uiController.loopMainThreadUntilIdle()
            val startTime = System.currentTimeMillis()
            val endTime = startTime + millis

            val rv = (view as RecyclerView)

            do {
                if (rv.adapter?.itemCount == itemCount) {
                    return
                }
                uiController.loopMainThreadForAtLeast(50)
            } while (System.currentTimeMillis() < endTime)

            throw PerformException.Builder()
                .withActionDescription(this.description)
                .withViewDescription(HumanReadables.describe(view))
                .withCause(TimeoutException())
                .build()
        }
    }
}

fun isTextMaxLines(maxLines: Int): TypeSafeMatcher<View> {
    return object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description?) {
            description?.appendText("line count to be $maxLines")
        }

        override fun matchesSafely(item: View?): Boolean {
            val lines = (item as? TextView)?.lineCount ?: return false
            return lines <= maxLines
        }
    }
}
