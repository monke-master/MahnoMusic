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
import androidx.recyclerview.widget.LinearLayoutManager
import com.monke.machnomusic3.R
import com.monke.machnomusic3.databinding.FragmentMyMusicBinding
import com.monke.machnomusic3.domain.model.mockedTracks
import com.monke.machnomusic3.main.activity.MainActivity
import com.monke.machnomusic3.ui.components.LoadingDialog
import com.monke.machnomusic3.ui.mainFeature.MainFragment
import com.monke.machnomusic3.ui.musicFeature.adapters.AlbumRWAdapter
import com.monke.machnomusic3.ui.musicFeature.adapters.TrackRWAdapter
import com.monke.machnomusic3.ui.uiModels.UiState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
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
        collectUiState()
    }

    private fun setupUploadAlbumButton() {
        binding?.btnUploadAlbum?.setOnClickListener {
            (parentFragment?.parentFragment as? MainFragment)?.mainNavController
                ?.navigate(R.id.action_mainFragment_to_uploadAlbumFragment)
        }
    }


    private fun setupAlbumsRecyclerList() {
        val albumAdapter = AlbumRWAdapter(
            onItemClicked = {

            }
        )

        binding?.recyclerAlbums?.adapter = albumAdapter
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