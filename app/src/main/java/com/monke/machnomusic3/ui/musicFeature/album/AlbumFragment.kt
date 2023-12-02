package com.monke.machnomusic3.ui.musicFeature.album

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.monke.machnomusic3.R
import com.monke.machnomusic3.databinding.FragmentAlbumBinding
import com.monke.machnomusic3.databinding.FragmentMyMusicBinding
import com.monke.machnomusic3.main.activity.MainActivity
import com.monke.machnomusic3.ui.components.LoadingDialog
import com.monke.machnomusic3.ui.musicFeature.adapters.AlbumTrackRWAdapter
import com.monke.machnomusic3.ui.musicFeature.adapters.TrackRWAdapter
import com.monke.machnomusic3.ui.uiModels.UiState
import kotlinx.coroutines.launch
import javax.inject.Inject

class AlbumFragment : Fragment() {

    @Inject
    lateinit var factory: AlbumViewModel.Factory
    private val viewModel: AlbumViewModel by viewModels { factory }

    private var binding: FragmentAlbumBinding? = null

    companion object {
        const val BUNDLE_KEY_ALBUM_ID = "com.monke.machnomusic3.ui.musicFeature.album.albumId"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)
        (activity as? MainActivity)?.mainComponent?.inject(this)
        val albumId = arguments?.getString(BUNDLE_KEY_ALBUM_ID)
        albumId?.let { viewModel.loadAlbum(albumId) }

        setupAlbumTitle()
        setupTracksRecyclerList()
        setupCoverImage()
        collectUiState()
        return binding?.root
    }


    private fun setupAlbumTitle() {
        binding?.txtTitle?.text = viewModel.album.value?.title
        binding?.txtAuthor?.text = viewModel.album.value?.author?.username
    }


    private fun setupCoverImage() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cover.collect { uri ->
                    val imageView = binding?.picAlbum ?: return@collect
                    Glide
                        .with(this@AlbumFragment)
                        .load(uri)
                        .into(imageView)
                }
            }
        }
    }


    private fun setupTracksRecyclerList() {
        val tracksAdapter = AlbumTrackRWAdapter(
            onItemClicked = { index ->
                viewModel.playTrackList(viewModel.tracksList.value, index)
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
                        is UiState.Success -> {}
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