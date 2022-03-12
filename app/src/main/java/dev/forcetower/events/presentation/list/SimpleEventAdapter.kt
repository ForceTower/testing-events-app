package dev.forcetower.events.presentation.list

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.forcetower.events.R
import dev.forcetower.events.databinding.ItemSimpleEventBinding
import dev.forcetower.events.domain.model.SimpleEvent
import dev.forcetower.events.tooling.extensions.inflate

class SimpleEventAdapter(
    private val actions: ListViewModel
) : ListAdapter<SimpleEvent, SimpleEventAdapter.EventHolder>(DiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SimpleEventAdapter.EventHolder {
        return EventHolder(parent.inflate(R.layout.item_simple_event), actions)
    }

    override fun onBindViewHolder(holder: SimpleEventAdapter.EventHolder, position: Int) {
        holder.binding.apply {
            item = getItem(position)
        }
    }

    inner class EventHolder(
        val binding: ItemSimpleEventBinding,
        actions: ListViewModel
    ) : RecyclerView.ViewHolder(binding.root) {
        init { binding.actions = actions }
    }

    private object DiffCallback : DiffUtil.ItemCallback<SimpleEvent>() {
        override fun areItemsTheSame(
            oldItem: SimpleEvent,
            newItem: SimpleEvent
        ) = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: SimpleEvent,
            newItem: SimpleEvent
        ) = oldItem == newItem
    }
}
