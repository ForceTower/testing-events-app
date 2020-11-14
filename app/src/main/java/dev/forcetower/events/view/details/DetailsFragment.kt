package dev.forcetower.events.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.events.databinding.FragmentDetailsBinding
import dev.forcetower.events.view.list.EventListViewModel
import dev.forcetower.toolkit.components.BaseFragment

@AndroidEntryPoint
class DetailsFragment : BaseFragment() {
    private val viewModel by viewModels<EventListViewModel>()
    private val args by navArgs<DetailsFragmentArgs>()
    private lateinit var binding: FragmentDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentDetailsBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.updateEvent(args.eventId)
        viewModel.getEvent(args.eventId).observe(viewLifecycleOwner, {

        })
    }
}