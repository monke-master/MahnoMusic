package com.monke.machnomusic3.ui.userFeature.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.bumptech.glide.Glide
import com.monke.machnomusic3.R
import com.monke.machnomusic3.databinding.FragmentProfileBinding
import com.monke.machnomusic3.main.activity.MainActivity
import com.monke.machnomusic3.ui.components.LoadingDialog
import com.monke.machnomusic3.ui.uiModels.UiState
import com.monke.machnomusic3.ui.userFeature.post.PostRWAdapter
import com.monke.machnomusic3.ui.userFeature.user.UsersListFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileFragment : Fragment() {

    @Inject
    lateinit var factory: ProfileViewModel.Factory
    private val viewModel: ProfileViewModel by viewModels { factory }

    private var binding: FragmentProfileBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        (activity as? MainActivity)?.mainComponent?.inject(this)

        setupUserData()
        setupProfilePicture()
        setupNewPostButton()
        collectUiState()
        setupPostsRecyclerList()
        return binding?.root
    }

    private fun setupUserData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.user.collect { user ->
                    if (user == null)
                        return@collect
                    binding?.txtBio?.text = user.bio
                    binding?.toolbar?.title = user.login

                    setupSubscribersText(user.subscribersIdsList)
                    setupSubscriptionsText(user.subscriptionsIdsList)
                }
            }
        }
    }

    private fun setupSubscribersText(subscribersList: List<String>) {
        val text = binding?.txtSubscribers ?: return
        text.text = getString(R.string.subscribers, subscribersList.size)
        text.setOnClickListener {
            val bundle = Bundle()
            bundle.putStringArray(
                UsersListFragment.BUNDLE_KEY_ID_LIST,
                subscribersList.toTypedArray()
            )
            findNavController().navigate(R.id.action_profileFragment_to_usersListFragment, bundle)
        }
    }

    private fun setupSubscriptionsText(subscriptionsList: List<String>) {
        val text = binding?.txtSubscriptions ?: return
        text.text = getString(R.string.subscriptions, subscriptionsList.size)
        text.setOnClickListener {
            val bundle = Bundle()
            bundle.putStringArray(
                UsersListFragment.BUNDLE_KEY_ID_LIST,
                subscriptionsList.toTypedArray()
            )
            findNavController().navigate(R.id.action_profileFragment_to_usersListFragment, bundle)
        }
    }

    private fun setupProfilePicture() {
        val pictureView = binding?.picProfile ?: return
        pictureView.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_profilePictureFragment)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.pictureUrl.collect { url ->
                    if (url == null) return@collect
                    Glide
                        .with(this@ProfileFragment)
                        .load(url)
                        .circleCrop()
                        .into(pictureView)
                }
            }
        }
    }


    private fun setupNewPostButton() {
        binding?.btnNewPost?.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_uploadPostFragment)
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
                viewModel.postsList.collect { postsList ->
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