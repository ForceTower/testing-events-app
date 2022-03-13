package dev.forcetower.events.tooling

import androidx.annotation.StringRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.CoreMatchers
import org.hamcrest.core.AllOf.allOf

fun onSnackbar(@StringRes withText: Int): ViewInteraction {
    return onView(
        CoreMatchers.allOf(
            ViewMatchers.withId(com.google.android.material.R.id.snackbar_text),
            ViewMatchers.withText(withText)
        )
    )
}

fun onSnackbarButton(@StringRes withText: Int): ViewInteraction {
    return onView(
        allOf(
            ViewMatchers.withId(com.google.android.material.R.id.snackbar_action),
            ViewMatchers.withText(withText)
        )
    )
}
