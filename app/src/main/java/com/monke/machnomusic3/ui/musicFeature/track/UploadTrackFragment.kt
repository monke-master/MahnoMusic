package com.monke.machnomusic3.ui.musicFeature.track

import android.content.Intent
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.monke.machnomusic3.data.files.AUDIO_FILES
import com.monke.machnomusic3.data.files.IMAGE_FILES
import com.monke.machnomusic3.databinding.FragmentUploadTrackBinding
import com.monke.machnomusic3.main.activity.MainActivity
import com.monke.machnomusic3.ui.components.LoadingDialog
import com.monke.machnomusic3.ui.uiModels.UiState
import kotlinx.coroutines.launch
import javax.inject.Inject


class UploadTrackFragment : Fragment() {

    companion object {

        const val PICK_AUDIO_CODE = 1
        const val PICK_IMAGE_CODE = 2

    }

    @Inject
    lateinit var factory: UploadTrackViewModel.Factory
    private val viewModel: UploadTrackViewModel by viewModels { factory }

    private var binding: FragmentUploadTrackBinding? = null
    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUploadTrackBinding.inflate(inflater, container, false)
        (activity as? MainActivity)?.mainComponent?.inject(this)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        setupTitleEditText()
        setupCoverImage()
        setupPickFileButton()
        collectUiState()
        setupConfirmButton()
    }

    private fun setupPickFileButton() {
        binding?.btnPickFile?.setOnClickListener {
            val intent = Intent()
            intent.type = AUDIO_FILES
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, PICK_AUDIO_CODE)
        }
    }


    private fun setupTitleEditText() {
        binding?.editTextTitle?.setText(viewModel.trackTitle.value)
        binding?.editTextTitle?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(text: Editable?) {
                viewModel.setTrackTitle(text?.toString() ?: "")
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val uri = data?.data
        if (resultCode == AppCompatActivity.RESULT_OK && uri != null) {
            when (requestCode) {
                PICK_AUDIO_CODE -> {
                    viewModel.trackDuration = getTrackDuration(uri) ?: 0
                    viewModel.setTrackUri(uri)
                }
                PICK_IMAGE_CODE -> viewModel.setCoverUri(uri)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setupCoverImage() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.coverUri.collect { cover ->
                    binding?.picCover?.setImageURI(cover)
                }
            }
        }

        binding?.picCover?.setOnClickListener {
            val intent = Intent()
            intent.type = IMAGE_FILES
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, PICK_IMAGE_CODE)
        }
    }

    private fun setupConfirmButton() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.trackUri.collect { uri ->
                        if (viewModel.trackTitle.value.isNotEmpty()) {
                            binding?.btnUpload?.visibility = View.VISIBLE
                        } else
                            binding?.btnUpload?.visibility = View.GONE
                    }
                }
                launch {
                    viewModel.trackTitle.collect { trackTitle ->
                        if (trackTitle.isNotEmpty() && viewModel.trackUri.value != Uri.EMPTY) {
                            binding?.btnUpload?.visibility = View.VISIBLE
                        } else
                            binding?.btnUpload?.visibility = View.GONE
                    }
                }

            }
        }
        binding?.btnUpload?.setOnClickListener {
            viewModel.uploadTrack()
        }
    }


    private fun getTrackDuration(uri: Uri): Int? {
        val mmr = MediaMetadataRetriever()
        mmr.setDataSource(requireContext(), uri)
        val durationStr =
            mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        return durationStr?.toInt()
    }

    private fun collectUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        UiState.Loading -> showLoadingDialog()
                        is UiState.Error ->
                            Toast.makeText(
                                requireContext(),
                                state.exception.message,
                                Toast.LENGTH_SHORT).
                            show()
                        is UiState.Success -> {
                            navController.popBackStack()
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun showLoadingDialog() {
        val dialog = LoadingDialog()
        dialog.show(parentFragmentManager, dialog.tag)
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    if (state !is UiState.Loading)
                        dialog.dismiss()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}