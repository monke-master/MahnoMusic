package com.monke.machnomusic3.ui.homeFeature

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
import androidx.recyclerview.widget.RecyclerView
import com.monke.machnomusic3.R
import com.monke.machnomusic3.databinding.FragmentAlbumBinding
import com.monke.machnomusic3.databinding.FragmentHomeBinding
import com.monke.machnomusic3.main.activity.MainActivity
import com.monke.machnomusic3.ui.components.LoadingDialog
import com.monke.machnomusic3.ui.musicFeature.album.AlbumViewModel
import com.monke.machnomusic3.ui.uiModels.UiState
import com.monke.machnomusic3.ui.userFeature.post.PostRWAdapter
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeFragment : Fragment() {

    @Inject
    lateinit var factory: HomeViewModel.Factory
    private val viewModel: HomeViewModel by viewModels { factory }

    private var binding: FragmentHomeBinding? = null
    ddddf

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        (activity as? MainActivity)?.mainComponent?.inject(this)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPostsRecyclerList()
        collectUiState()
    }

    private fun setupPostsRecyclerList() {
        val adapter = PostRWAdapter(
            onTrackClicked = { tracks, index ->
                viewModel.playTrackList(tracks, index)
            }
        )

        binding?.recyclerPosts?.adapter = adapter
        binding?.recyclerPosts?.layoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.VERTICAL,
            false
        )

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.posts.collect { postsList ->
                    adapter.postsList = postsList
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
                        is UiState.Success -> {
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

}