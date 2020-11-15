package dev.forcetower.events.view.checkin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.events.R
import dev.forcetower.events.databinding.FragmentCheckInBinding
import dev.forcetower.toolkit.components.BaseSheetDialogFragment
import dev.forcetower.toolkit.lifecycle.EventObserver

@AndroidEntryPoint
class CheckInFragment : BaseSheetDialogFragment() {
    private val viewModel by viewModels<CheckInViewModel>()
    private val args by navArgs<CheckInFragmentArgs>()
    private lateinit var binding: FragmentCheckInBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = FragmentCheckInBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            actions = viewModel
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.onRegistered.observe(
            viewLifecycleOwner,
            EventObserver {
                showSnack(getString(R.string.registered_to_event))
                dismiss()
            }
        )
        viewModel.setEventId(args.eventId)
    }
}
