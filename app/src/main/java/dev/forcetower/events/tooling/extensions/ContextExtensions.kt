package dev.forcetower.events.tooling.extensions

import android.content.Context
import androidx.annotation.AttrRes

fun Context.resolveColorAttr(@AttrRes attribute: Int): Int {
    val typedValue = obtainStyledAttributes(intArrayOf(attribute))
    val color = typedValue.getColor(0, 0)
    typedValue.recycle()
    return color
}