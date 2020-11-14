package dev.forcetower.events.view.details

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.events.R
import dev.forcetower.events.databinding.FragmentDetailsBinding
import dev.forcetower.events.view.list.EventListViewModel
import dev.forcetower.toolkit.components.BaseFragment

@AndroidEntryPoint
class DetailsFragment : BaseFragment() {
    private val viewModel by viewModels<EventDetailsViewModel>()
    private val args by navArgs<DetailsFragmentArgs>()
    private lateinit var binding: FragmentDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = FragmentDetailsBinding.inflate(inflater, container, false).also {
            binding = it
        }.root

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.share -> {
                    shareEvent()
                    true
                }
                else -> false
            }
        }

        return view
    }

    private fun shareEvent() {
        val text = viewModel.getShareText()
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.updateEvent(args.eventId)
        viewModel.getEvent(args.eventId).observe(viewLifecycleOwner, {
            binding.event = it
        })
    }
}