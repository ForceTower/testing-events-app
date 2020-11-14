package dev.forcetower.events.view.details

import android.widget.TextView
import androidx.databinding.BindingAdapter
import dev.forcetower.events.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@BindingAdapter("eventDate")
fun TextView.eventDate(date: LocalDateTime?) {
    date ?: return
    val pattern = "EEE, dd MMMM yyyy HH:mm"
    text = context.getString(R.string.date_format, date.format(DateTimeFormatter.ofPattern(pattern)))
}