package dev.forcetower.events

import android.os.Bundle
import androidx.core.view.WindowCompat
import dev.forcetower.toolkit.components.BaseActivity

class HomeActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }
}