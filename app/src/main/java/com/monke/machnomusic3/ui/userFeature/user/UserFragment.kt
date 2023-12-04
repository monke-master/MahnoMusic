package com.monke.machnomusic3.ui.userFeature.user

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.monke.machnomusic3.R
import com.monke.machnomusic3.databinding.FragmentProfileBinding
import com.monke.machnomusic3.databinding.FragmentUserBinding
import com.monke.machnomusic3.main.activity.MainActivity
import com.monke.machnomusic3.ui.components.LoadingDialog
import com.monke.machnomusic3.ui.uiModels.UiState
import com.monke.machnomusic3.ui.userFeature.post.PostRWAdapter
import com.monke.machnomusic3.ui.userFeature.profile.ProfileViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserFragment : Fragment() {

    companion object {

        const val BUNDLE_KEY_USER_ID = "com.monke.machnomusic3.ui.userFeature.user.userId"

    }

    @Inject
    lateinit var factory: UserViewModel.Factory
    private val viewModel: UserViewModel by viewModels { factory }

    private var binding: FragmentUserBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(
            inflater,
            container,
            false
        )
        (activity as? MainActivity)?.mainComponent?.inject(this)

        arguments?.getString(BUNDLE_KEY_USER_ID)?.let { userId -> viewModel.loadUserData(userId) }

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUserData()
        setupProfilePicture()
        collectUiState()
        setupPostsRecyclerList()

    }

    private fun setupUserData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.user.collect { user ->
                    if (user == null)
                        return@collect
                    binding?.txtBio?.text = user.bio
                    binding?.toolbar?.title = user.login
                }
            }
        }
    }

    private fun setupProfilePicture() {
        val pictureView = binding?.picProfile ?: return

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.pictureUrl.collect { url ->
                    Glide
                        .with(this@UserFragment)
                        .load(url)
                        .circleCrop()
                        .into(pictureView)
                }
            }
        }
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
                viewModel.postsList.collect {
                    adapter.postsList = it
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