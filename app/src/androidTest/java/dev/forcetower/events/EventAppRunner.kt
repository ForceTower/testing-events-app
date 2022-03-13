package dev.forcetower.events

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

class EventAppRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader, name: String?, context: Context): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}
