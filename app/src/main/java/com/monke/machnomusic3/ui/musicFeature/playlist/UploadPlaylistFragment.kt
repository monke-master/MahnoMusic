package com.monke.machnomusic3.ui.musicFeature.playlist

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.monke.machnomusic3.R
import com.monke.machnomusic3.data.files.AUDIO_FILES
import com.monke.machnomusic3.data.files.IMAGE_FILES
import com.monke.machnomusic3.databinding.FragmentUploadAlbumBinding
import com.monke.machnomusic3.databinding.FragmentUploadPlaylistBinding
import com.monke.machnomusic3.main.activity.MainActivity
import com.monke.machnomusic3.ui.components.LoadingDialog
import com.monke.machnomusic3.ui.musicFeature.adapters.TrackRWAdapter
import com.monke.machnomusic3.ui.musicFeature.album.UploadAlbumFragment
import com.monke.machnomusic3.ui.musicFeature.album.UploadAlbumViewModel
import com.monke.machnomusic3.ui.musicFeature.track.SelectTracksFragment
import com.monke.machnomusic3.ui.musicFeature.track.UploadTrackFragment
import com.monke.machnomusic3.ui.uiModels.UiState
import com.monke.machnomusic3.ui.userFeature.post.UploadPostFragment
import kotlinx.coroutines.launch
import javax.inject.Inject

class UploadPlaylistFragment : Fragment() {

    companion object {

        const val PICK_IMAGE_CODE = 0

    }

    @Inject
    lateinit var factory: UploadPlaylistViewModel.Factory
    private val viewModel: UploadPlaylistViewModel by viewModels { factory }

    private var binding: FragmentUploadPlaylistBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(
            SelectTracksFragment.SELECT_TRACK
        ) { _, bundle ->
            bundle.getString(SelectTracksFragment.BUNDLE_KEY_SELECTED_ID)?.let { trackId ->
                viewModel.addTrack(trackId)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUploadPlaylistBinding.inflate(inflater, container, false)
        (activity as? MainActivity)?.mainComponent?.inject(this)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCoverImage()
        setupTitleEditText()
        setupUploadTrackBtn()
        setupDescriptionEditText()
        setupTracksRecyclerList()
        setupUploadBtn()

        collectUiState()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val uri = data?.data
        if (resultCode == AppCompatActivity.RESULT_OK && uri != null) {
            when (requestCode) {
                PICK_IMAGE_CODE -> viewModel.setCoverUri(uri)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setupCoverImage() {
        val coverView = binding?.imgCover ?: return

        coverView.setOnClickListener {
            val intent = Intent()
            intent.type = IMAGE_FILES
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, PICK_IMAGE_CODE)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.coverUri.collect { uri ->
                    if (uri == Uri.EMPTY) return@collect
                    coverView.setImageURI(uri)
                }
            }
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

    private fun setupDescriptionEditText() {
        binding?.editTextDescription?.setText(viewModel.title.value)
        binding?.editTextDescription?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(text: Editable?) {
                viewModel.setDescription(text?.toString())
            }
        })
    }


    private fun setupUploadTrackBtn() {
        binding?.btnUploadTrack?.setOnClickListener {
            findNavController().navigate(R.id.action_uploadPlaylistFragment_to_selectTracksFragment)
        }
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
                        is UiState.Success -> findNavController().popBackStack()
                        else -> {}
                    }
                }
            }
        }
    }

    private fun setupTracksRecyclerList() {
        val tracksAdapter = TrackRWAdapter(
            onItemClicked = { index ->

            }
        )

        binding?.recyclerTracks?.adapter = tracksAdapter
        binding?.recyclerTracks?.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.tracksList.collect {
                    tracksAdapter.tracksList = it
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
            viewModel.uploadPlaylist()
        }
    }

}