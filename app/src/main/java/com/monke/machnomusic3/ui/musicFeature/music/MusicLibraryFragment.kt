package com.monke.machnomusic3.ui.musicFeature.music

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.monke.machnomusic3.R
import com.monke.machnomusic3.databinding.FragmentMiniPlayerBinding
import com.monke.machnomusic3.databinding.FragmentMusicLibraryBinding
import com.monke.machnomusic3.ui.musicFeature.musicPlayer.MiniPlayerViewModel
import javax.inject.Inject

class MusicLibraryFragment : Fragment() {

    @Inject
    lateinit var factory: MusicLibraryViewModel.Factory
    private val viewModel: MusicLibraryViewModel by viewModels { factory }

    private var binding: FragmentMusicLibraryBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMusicLibraryBinding.inflate(inflater, container, false)
        return binding?.root
    }




}