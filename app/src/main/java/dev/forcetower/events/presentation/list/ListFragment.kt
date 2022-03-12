package dev.forcetower.events.presentation.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.events.R
import dev.forcetower.events.databinding.FragmentListBinding
import dev.forcetower.events.tooling.lifecycle.EventObserver

@AndroidEntryPoint
class ListFragment : Fragment() {
    private val viewModel by viewModels<ListViewModel>()
    private lateinit var adapter: SimpleEventAdapter
    private lateinit var binding: FragmentListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        adapter = SimpleEventAdapter(viewModel)
        return FragmentListBinding.inflate(inflater, container, false).also {
            binding = it
            binding.apply {
                actions = viewModel
                recyclerEvents.adapter = adapter
                lifecycleOwner = viewLifecycleOwner
            }
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.events.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.onRefreshFailed.observe(
            viewLifecycleOwner,
            EventObserver {
                Snackbar.make(
                    binding.root,
                    R.string.events_list_failed_to_refresh,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        )

        viewModel.onEventSelected.observe(
            viewLifecycleOwner,
            EventObserver {
                val directions = ListFragmentDirections.actionListToDetails(it)
                findNavController().navigate(directions)
            }
        )
    }
}
