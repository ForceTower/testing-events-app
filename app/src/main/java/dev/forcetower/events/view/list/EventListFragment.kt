package dev.forcetower.events.view.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.events.databinding.FragmentEventListBinding
import dev.forcetower.toolkit.components.BaseFragment
import dev.forcetower.toolkit.lifecycle.EventObserver

@AndroidEntryPoint
class EventListFragment : BaseFragment() {
    private val viewModel by viewModels<EventListViewModel>()
    private lateinit var binding: FragmentEventListBinding
    private lateinit var adapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = FragmentEventListBinding.inflate(inflater, container, false).also {
            binding = it
        }.root

        binding.apply {
            actions = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        adapter = EventAdapter(viewModel)
        binding.eventsRecycler.apply {
            adapter = this@EventListFragment.adapter
            itemAnimator?.changeDuration = 0L
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.onUpdateError.observe(viewLifecycleOwner, EventObserver {
            showSnack(getString(it))
        })

        viewModel.onEventSelected.observe(viewLifecycleOwner, EventObserver {
            val directions = EventListFragmentDirections.actionListToDetails(it.id)
            findNavController().navigate(directions)
        })

        viewModel.getEvents().observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })

        viewModel.updateList()
    }
}