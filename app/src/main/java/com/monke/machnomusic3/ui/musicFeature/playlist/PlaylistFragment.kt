package com.monke.machnomusic3.ui.musicFeature.playlist

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
import com.monke.machnomusic3.databinding.FragmentPlaylistBinding
import com.monke.machnomusic3.main.activity.MainActivity
import com.monke.machnomusic3.ui.components.LoadingDialog
import com.monke.machnomusic3.ui.musicFeature.adapters.TrackRWAdapter
import com.monke.machnomusic3.ui.uiModels.UiState
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlaylistFragment : Fragment() {

    @Inject
    lateinit var factory: PlaylistViewModel.Factory
    private val viewModel: PlaylistViewModel by viewModels { factory }

    private var binding: FragmentPlaylistBinding? = null

    companion object {
        const val BUNDLE_KEY_PLAYLIST_ID =
            "com.monke.machnomusic3.ui.musicFeature.playlist.playlistId"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        (activity as? MainActivity)?.mainComponent?.inject(this)
        val playlistId = arguments?.getString(BUNDLE_KEY_PLAYLIST_ID)
        playlistId?.let { viewModel.loadPlaylist(playlistId) }

        setupPlaylistTitle()
        setupTracksRecyclerList()
        setupCoverImage()
        collectUiState()
        return binding?.root
    }


    private fun setupPlaylistTitle() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.playlist.collect { playlist ->
                    if (playlist == null) return@collect
                    binding?.txtTitle?.text = playlist.title
                    binding?.txtAuthor?.text = playlist.author.username
                }
            }
        }
    }


    private fun setupCoverImage() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cover.collect { uri ->
                    val imageView = binding?.picAlbum ?: return@collect
                    Glide
                        .with(this@PlaylistFragment)
                        .load(uri)
                        .into(imageView)
                }
            }
        }
    }


    private fun setupTracksRecyclerList() {
        val tracksAdapter = TrackRWAdapter(
            onItemClicked = { index ->
                viewModel.playTrackList(viewModel.tracksList.value.map { it.track }, index)
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