package dev.forcetower.events.presentation.checkin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.events.databinding.FragmentCheckInBinding
import dev.forcetower.events.tooling.components.BaseBottomSheetDialogFragment
import dev.forcetower.events.tooling.lifecycle.EventObserver
import javax.inject.Inject

@AndroidEntryPoint
class CheckInFragment : BaseBottomSheetDialogFragment() {
    @Inject lateinit var factory: CheckInViewModelFactory
    private val args by navArgs<CheckInFragmentArgs>()
    private val viewModel by viewModels<CheckInViewModel> { provideFactory(factory, args.id) }

    private lateinit var binding: FragmentCheckInBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentCheckInBinding.inflate(inflater, container, false).also {
            binding = it
            binding.apply {
                actions = viewModel
                lifecycleOwner = viewLifecycleOwner
            }
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onCompleted.observe(
            viewLifecycleOwner,
            EventObserver {
                findNavController().popBackStack()
            }
        )
    }
}
