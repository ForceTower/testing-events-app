package dev.forcetower.events.presentation.share

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.events.databinding.FragmentShareBinding
import dev.forcetower.events.tooling.components.BaseBottomSheetDialogFragment

@AndroidEntryPoint
class ShareFragment : BaseBottomSheetDialogFragment() {
    private lateinit var binding: FragmentShareBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentShareBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnComplete.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
