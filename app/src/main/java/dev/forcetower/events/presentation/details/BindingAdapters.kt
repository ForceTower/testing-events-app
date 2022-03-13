package dev.forcetower.events.presentation.details

import android.widget.TextView
import androidx.databinding.BindingAdapter
import timber.log.Timber
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@BindingAdapter("eventDate")
fun TextView.eventDate(date: LocalDateTime?) {
    text = date?.let {
        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT).format(it)
    }
}
