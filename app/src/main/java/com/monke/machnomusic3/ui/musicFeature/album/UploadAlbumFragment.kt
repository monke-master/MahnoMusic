package com.monke.machnomusic3.ui.musicFeature.album

import android.content.Intent
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.monke.machnomusic3.R
import com.monke.machnomusic3.data.files.AUDIO_FILES
import com.monke.machnomusic3.data.files.IMAGE_FILES
import com.monke.machnomusic3.databinding.FragmentUploadAlbumBinding
import com.monke.machnomusic3.databinding.FragmentUploadTrackBinding
import com.monke.machnomusic3.domain.usecase.album.UploadAlbumUseCase
import com.monke.machnomusic3.main.activity.MainActivity
import com.monke.machnomusic3.ui.components.LoadingDialog
import com.monke.machnomusic3.ui.musicFeature.adapters.UploadingTrackRWAdapter
import com.monke.machnomusic3.ui.musicFeature.track.UploadTrackFragment
import com.monke.machnomusic3.ui.musicFeature.track.UploadTrackViewModel
import com.monke.machnomusic3.ui.uiModels.UiState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class UploadAlbumFragment() : Fragment() {

    companion object {

        const val PICK_AUDIO_CODE = 1
        const val PICK_IMAGE_CODE = 2

    }

    @Inject
    lateinit var factory: UploadAlbumViewModel.Factory
    private val viewModel: UploadAlbumViewModel by viewModels { factory }

    private var binding: FragmentUploadAlbumBinding? = null
    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUploadAlbumBinding.inflate(inflater, container, false)
        (activity as? MainActivity)?.mainComponent?.inject(this)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        setupUploadTrackBtn()
        setupTitleEditText()
        setupTrackRecycler()
        setupPickCoverBtn()
        setupUploadBtn()
        collectUiState()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val uri = data?.data
        if (resultCode == AppCompatActivity.RESULT_OK && uri != null) {
            when (requestCode) {
                PICK_AUDIO_CODE -> pickTrack(uri)
                PICK_IMAGE_CODE -> viewModel.setCoverUri(uri)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setupUploadTrackBtn() {
        binding?.btnUploadTrack?.setOnClickListener {
            val intent = Intent()
            intent.type = AUDIO_FILES
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, PICK_AUDIO_CODE)
        }
    }


    private fun setupTrackRecycler() {
        val adapter = UploadingTrackRWAdapter(
            onItemClicked = {

            }
        )

        binding?.recyclerTracks?.adapter = adapter
        binding?.recyclerTracks?.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.tracksList.collect {
                    adapter.tracksList = it
                }
            }
        }
    }

    private fun setupPickCoverBtn() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.coverUri.collect { cover ->
                    if (cover != Uri.EMPTY)
                        binding?.picAlbum?.setImageURI(cover)
                }
            }
        }

        binding?.picAlbum?.setOnClickListener {
            val intent = Intent()
            intent.type = IMAGE_FILES
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, PICK_IMAGE_CODE)
        }
    }

    private fun setupTitleEditText() {
        binding?.editTextTitle?.setText(viewModel.title.value)
        binding?.editTextTitle?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(text: Editable?) {
                viewModel.setTitle(text?.toString() ?: "")
            }
        })
    }

    private fun pickTrack(uri: Uri) {
        val dialog = TitleTrackDialog()
        dialog.show(childFragmentManager, dialog.tag)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.trackTitle.collect { trackTitle ->
                    if (trackTitle.isNotEmpty()) {
                        val duration = getTrackDuration(uri) ?: 0
                        viewModel.addTrack(uri, duration)
                    }
                }
            }
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

    private fun setupUploadBtn() {
        binding?.btnUpload?.setOnClickListener {
            viewModel.uploadAlbum()
        }
    }

}