package dev.forcetower.events.view.list

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.forcetower.events.R
import dev.forcetower.events.core.model.Event
import dev.forcetower.events.databinding.ItemEventBinding
import dev.forcetower.toolkit.extensions.inflate

class EventAdapter(
    private val actions: EventListActions
) : ListAdapter<Event, EventAdapter.EventHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventAdapter.EventHolder {
        return EventHolder(parent.inflate(R.layout.item_event), actions)
    }

    override fun onBindViewHolder(holder: EventAdapter.EventHolder, position: Int) {
        holder.binding.event = getItem(position)
    }

    inner class EventHolder(
        val binding: ItemEventBinding,
        actions: EventListActions
    ) : RecyclerView.ViewHolder(binding.root) {
        init { binding.actions = actions }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Event, newItem: Event) = oldItem == newItem
    }
}
