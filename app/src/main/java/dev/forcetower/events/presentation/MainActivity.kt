package dev.forcetower.events.presentation

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.events.R
import dev.forcetower.events.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Window insets listeners were added on API 20.
        // Most of the code for handling insets rely on this method.
        // https://developer.android.com/reference/android/view/View.html#setOnApplyWindowInsetsListener(android.view.View.OnApplyWindowInsetsListener)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            WindowCompat.setDecorFitsSystemWindows(window, false)

        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    override fun onSupportNavigateUp() = findNavController(R.id.fragment_container).navigateUp()
}