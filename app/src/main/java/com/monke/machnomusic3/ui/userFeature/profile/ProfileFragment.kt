package com.monke.machnomusic3.ui.userFeature.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.monke.machnomusic3.databinding.FragmentProfileBinding
import com.monke.machnomusic3.main.activity.MainActivity
import kotlinx.coroutines.flow.collect
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
        return binding?.root
    }

    private fun setupUserData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.user.collect { user ->
                    if (user == null)
                        return@collect
                    binding?.txtName?.text = user.username
                    binding?.txtBio?.text = user.bio
                    binding?.toolbar?.title = user.login
                }
            }
        }
    }


    private fun setupNewPostButton() {

    }

}