package dev.forcetower.events.tooling.extensions

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

fun View.inflater(): LayoutInflater = LayoutInflater.from(context)

inline fun <reified T : ViewDataBinding> ViewGroup.inflate(@LayoutRes res: Int, attachToParent: Boolean = false): T {
    val inflater = inflater()
    return DataBindingUtil.inflate(inflater, res, this, attachToParent)
}

inline fun <reified T : ViewDataBinding> LayoutInflater.inflate(@LayoutRes res: Int): T {
    return DataBindingUtil.inflate(this, res, null, false)
}

inline fun <T : ViewDataBinding> T.executeBindingsAfter(block: T.() -> Unit) {
    block()
    executePendingBindings()
}

fun RecyclerView.clearDecorations() {
    if (itemDecorationCount > 0) {
        for (i in itemDecorationCount - 1 downTo 0) {
            removeItemDecorationAt(i)
        }
    }
}

fun Activity.windowInsetsControllerCompat(view: View): WindowInsetsControllerCompat? {
    return WindowCompat.getInsetsController(window, view)
}

val View.windowInsetsControllerCompat: WindowInsetsControllerCompat?
    get() = ViewCompat.getWindowInsetsController(this)

fun View.closeKeyboard() {
    windowInsetsControllerCompat?.hide(WindowInsetsCompat.Type.ime())
}

fun View.openKeyboard() {
    windowInsetsControllerCompat?.show(WindowInsetsCompat.Type.ime())
}

fun View.fadeIn() {
    val fadeInAnim = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
    visibility = View.VISIBLE
    startAnimation(fadeInAnim)
    requestLayout()
}

fun View.doOnApplyWindowInsets(f: (View, WindowInsetsCompat, InitialPadding) -> Unit) {
    val initialPadding = recordInitialPaddingForView(this)
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        f(v, insets, initialPadding)
        insets
    }
    requestApplyInsetsWhenAttached()
}

fun View.doOnApplyWindowMarginInsets(f: (View, WindowInsetsCompat, InitialPadding) -> Unit) {
    val initialMargin = recordInitialMarginForView(this)
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        f(v, insets, initialMargin)
        insets
    }
    requestApplyInsetsWhenAttached()
}

private fun recordInitialPaddingForView(view: View) = InitialPadding(
    view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom
)

private fun recordInitialMarginForView(view: View) = InitialPadding(
    view.marginLeft, view.marginTop, view.marginRight, view.marginBottom
)

fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        ViewCompat.requestApplyInsets(this)
    } else {
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                ViewCompat.requestApplyInsets(v)
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}

@BindingAdapter("refreshing")
fun swipeRefreshing(refreshLayout: SwipeRefreshLayout, refreshing: Boolean) {
    refreshLayout.isRefreshing = refreshing
}

@BindingAdapter("onSwipeRefresh")
fun onSwipeRefresh(view: SwipeRefreshLayout, function: SwipeRefreshLayout.OnRefreshListener) {
    view.setOnRefreshListener(function)
}

data class InitialPadding(
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int
)
