package com.monke.machnomusic3.ui.userFeature.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.monke.machnomusic3.databinding.FragmentProfileBinding
import com.monke.machnomusic3.main.activity.MainActivity
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
        return binding?.root
    }




}