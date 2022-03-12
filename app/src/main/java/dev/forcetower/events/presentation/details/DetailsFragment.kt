package dev.forcetower.events.presentation.details

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.events.R
import dev.forcetower.events.databinding.FragmentDetailsBinding
import dev.forcetower.events.tooling.lifecycle.EventObserver
import javax.inject.Inject

private const val CLIPBOARD_SHARE_LABEL = "events:share:details"

@AndroidEntryPoint
class DetailsFragment : Fragment() {
    @Inject lateinit var factory: DetailsViewModelFactory
    private lateinit var binding: FragmentDetailsBinding

    private val args by navArgs<DetailsFragmentArgs>()
    private val viewModel by viewModels<DetailsViewModel> { provideFactory(factory, args.id) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentDetailsBinding.inflate(inflater, container, false).also {
            binding = it
            binding.apply {
                actions = viewModel
                lifecycleOwner = viewLifecycleOwner
            }
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onOpenMap.observe(
            viewLifecycleOwner,
            EventObserver {
                openMaps(it.first, it.second)
            }
        )

        viewModel.onShareInfo.observe(
            viewLifecycleOwner,
            EventObserver {
                onShare(it)
                val directions = DetailsFragmentDirections.actionDetailsToShare()
                findNavController().navigate(directions)
            }
        )

        viewModel.onCheckIn.observe(
            viewLifecycleOwner,
            EventObserver {
                val directions = DetailsFragmentDirections.actionDetailsToCheckIn(it)
                findNavController().navigate(directions)
            }
        )

        viewModel.onRefreshFailed.observe(
            viewLifecycleOwner,
            EventObserver {
                Snackbar.make(
                    binding.root,
                    R.string.events_details_failed_to_refresh,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        )

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun onShare(content: String) {
        val manager = requireContext().getSystemService<ClipboardManager>() ?: return
        manager.setPrimaryClip(ClipData.newPlainText(CLIPBOARD_SHARE_LABEL, content))
    }

    private fun openMaps(latitude: Double, longitude: Double) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:$latitude,$longitude"))
            requireContext().startActivity(intent)
        } catch (error: Throwable) {
            openBrowser(latitude, longitude)
        }
    }

    private fun openBrowser(latitude: Double, longitude: Double) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=$latitude,$longitude"))
            requireContext().startActivity(intent)
        } catch (error: Throwable) {
            Snackbar.make(
                binding.root,
                R.string.events_details_failed_to_open_maps_or_browser,
                Snackbar.LENGTH_LONG
            ).show()
        }
    }
}
