package com.monke.machnomusic3.ui.musicFeature.music.myMusic

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
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.monke.machnomusic3.R
import com.monke.machnomusic3.databinding.FragmentMyMusicBinding
import com.monke.machnomusic3.main.activity.MainActivity
import com.monke.machnomusic3.ui.components.LoadingDialog
import com.monke.machnomusic3.ui.mainFeature.MainFragment
import com.monke.machnomusic3.ui.musicFeature.adapters.AlbumRWAdapter
import com.monke.machnomusic3.ui.musicFeature.adapters.PlaylistRWAdapter
import com.monke.machnomusic3.ui.musicFeature.adapters.TrackRWAdapter
import com.monke.machnomusic3.ui.musicFeature.album.AlbumFragment
import com.monke.machnomusic3.ui.musicFeature.playlist.PlaylistFragment
import com.monke.machnomusic3.ui.recyclerViewUtils.HorizontalSpaceItemDecoration
import com.monke.machnomusic3.ui.uiModels.UiState
import kotlinx.coroutines.launch
import javax.inject.Inject

class MyMusicFragment : Fragment() {


    @Inject
    lateinit var factory: MyMusicViewModel.Factory
    private val viewModel: MyMusicViewModel by viewModels { factory }

    private var binding: FragmentMyMusicBinding? = null
    lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyMusicBinding.inflate(inflater, container, false)
        (activity as? MainActivity)?.mainComponent?.inject(this)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        setupUploadAlbumButton()
        setupUploadTrackButton()
        setupTracksRecyclerList()
        setupUploadPlaylistButton()
        setupPlaylistsRecyclerList()
        setupAlbumsRecyclerList()
        setupSearchEditText()
        collectUiState()
    }

    private fun setupSearchEditText() {
        binding?.editTextSearch?.setOnFocusChangeListener { view, focused ->
            if (focused)
                findNavController().navigate(R.id.action_myMusicFragment_to_searchMusicFragment)
        }
    }

    private fun setupUploadAlbumButton() {
        binding?.btnUploadAlbum?.setOnClickListener {
            (parentFragment?.parentFragment as? MainFragment)?.mainNavController
                ?.navigate(R.id.action_mainFragment_to_uploadAlbumFragment)
        }
    }

    private fun setupUploadPlaylistButton() {
        binding?.btnUploadPlaylist?.setOnClickListener {
           findNavController().navigate(R.id.action_myMusicFragment_to_uploadPlaylistFragment)
        }
    }


    private fun setupAlbumsRecyclerList() {
        val albumAdapter = AlbumRWAdapter(
            onItemClicked = { albumId ->
                val bundle = Bundle()
                bundle.putString(AlbumFragment.BUNDLE_KEY_ALBUM_ID, albumId)
                navController.navigate(R.id.action_myMusicFragment_to_albumFragment, bundle)
            }
        )

        binding?.recyclerAlbums?.adapter = albumAdapter
        binding?.recyclerAlbums?.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        // Space decorator
        val spaceDecoration = HorizontalSpaceItemDecoration(
            horizontalPadding = resources.getDimensionPixelSize(R.dimen.album_padding)
        )
        binding?.recyclerAlbums?.addItemDecoration(spaceDecoration)
        // Подписывается на изменение списка альбомов
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.albumsList.collect {
                    albumAdapter.albumsList = it
                }
            }
        }
    }

    private fun setupPlaylistsRecyclerList() {
        val recyclerView = binding?.recyclerPlaylists ?: return
        val playlistAdapter = PlaylistRWAdapter(
            onItemClicked = { playlistId ->
                val bundle = Bundle()
                bundle.putString(PlaylistFragment.BUNDLE_KEY_PLAYLIST_ID, playlistId)
                navController.navigate(R.id.action_myMusicFragment_to_playlistFragment, bundle)
            }
        )

        recyclerView.adapter = playlistAdapter
        recyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        // Space decorator
        val spaceDecoration = HorizontalSpaceItemDecoration(
            horizontalPadding = resources.getDimensionPixelSize(R.dimen.album_padding)
        )
        recyclerView.addItemDecoration(spaceDecoration)
        // Подписывается на изменение списка альбомов
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.playlistsList.collect {
                    playlistAdapter.playlistItems = it
                }
            }
        }
    }



    private fun setupUploadTrackButton() {
        binding?.btnUploadTrack?.setOnClickListener {
            (parentFragment?.parentFragment as? MainFragment)?.mainNavController
                ?.navigate(R.id.action_mainFragment_to_uploadTrackFragment)
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