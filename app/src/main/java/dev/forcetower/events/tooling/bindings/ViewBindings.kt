package dev.forcetower.events.tooling.bindings

import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import dev.forcetower.events.tooling.extensions.doOnApplyWindowInsets
import dev.forcetower.events.tooling.extensions.doOnApplyWindowMarginInsets

@BindingAdapter(
    "paddingStartSystemWindowInsets",
    "paddingTopSystemWindowInsets",
    "paddingEndSystemWindowInsets",
    "paddingBottomSystemWindowInsets",
    "consumeSystemWindowInsets",
    requireAll = false
)
fun View.applySystemWindows(
    applyLeft: Boolean,
    applyTop: Boolean,
    applyRight: Boolean,
    applyBottom: Boolean,
    consumeInsets: Boolean = false
) {
    doOnApplyWindowInsets { _, allInsets, padding ->
        val insets = allInsets.getInsets(WindowInsetsCompat.Type.systemBars())
        val left = if (applyLeft) insets.left else 0
        val top = if (applyTop) insets.top else 0
        val right = if (applyRight) insets.right else 0
        val bottom = if (applyBottom) insets.bottom else 0

        setPadding(
            padding.left + left,
            padding.top + top,
            padding.right + right,
            padding.bottom + bottom
        )

        if (consumeInsets) {
            WindowInsetsCompat.CONSUMED
        }
    }
}

@BindingAdapter(
    "marginStartSystemWindowInsets",
    "marginTopSystemWindowInsets",
    "marginEndSystemWindowInsets",
    "marginBottomSystemWindowInsets",
    requireAll = false
)
fun View.applyMarginSystemWindows(
    applyLeft: Boolean,
    applyTop: Boolean,
    applyRight: Boolean,
    applyBottom: Boolean
) {
    doOnApplyWindowMarginInsets { _, allInsets, margins ->
        val insets = allInsets.getInsets(WindowInsetsCompat.Type.systemBars())
        val left = if (applyLeft) insets.left else 0
        val top = if (applyTop) insets.top else 0
        val right = if (applyRight) insets.right else 0
        val bottom = if (applyBottom) insets.bottom else 0

        (layoutParams as? ViewGroup.MarginLayoutParams)?.apply {
            setMargins(
                margins.left + left,
                margins.top + top,
                margins.right + right,
                margins.bottom + bottom
            )
        }?.also {
            layoutParams = it
        }
    }
}

@BindingAdapter("goneIf")
fun View.goneIf(gone: Boolean) {
    isVisible = !gone
}

@BindingAdapter("goneUnless")
fun View.goneUnless(condition: Boolean) {
    isVisible = condition
}

@BindingAdapter("invisibleUnless")
fun View.invisibleUnless(visible: Boolean) {
    isInvisible = !visible
}

@BindingAdapter("viewSelected")
fun View.viewSelected(selected: Boolean?) {
    val value = selected ?: false
    isSelected = value
}
