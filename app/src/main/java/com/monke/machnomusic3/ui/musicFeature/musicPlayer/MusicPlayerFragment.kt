package com.monke.machnomusic3.ui.musicFeature.musicPlayer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.monke.machnomusic3.R
import com.monke.machnomusic3.databinding.FragmentMusicPlayerBinding
import com.monke.machnomusic3.domain.model.MusicState
import com.monke.machnomusic3.domain.model.Track
import com.monke.machnomusic3.main.activity.MainActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class MusicPlayerFragment : Fragment() {

    @Inject
    lateinit var factory: MusicPlayerViewModel.Factory
    private val viewModel: MusicPlayerViewModel by viewModels { factory }

    private var binding: FragmentMusicPlayerBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMusicPlayerBinding.inflate(inflater, container, false)
        (activity as? MainActivity)?.mainComponent()?.inject(this)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.musicState.collect {state ->
                        when (state) {
                            MusicState.Pause -> {
                                updateActionButton(isPlaying = false)
                            }
                            MusicState.Resume -> {
                                updateActionButton(isPlaying = true)
                            }
                            MusicState.Start -> {
                                updateActionButton(isPlaying = true)
                            }
                            MusicState.Stop -> {

                            }
                        }
                    }
                }
                launch {
                    viewModel.track.collect { track ->
                        track?.let {
                            setTrackInfo(it)
                        }
                    }
                }

            }
        }
    }

    private fun setupActionButtons() {
        binding?.btnAction?.setOnClickListener {
            viewModel.updateState()
        }

        binding?.btnNext?.setOnClickListener {
            viewModel.nextTrack()
        }

        binding?.btnPrev?.setOnClickListener {
            viewModel.prevTrack()
        }
    }

    private fun setTrackInfo(track: Track) {
        binding?.txtTitle?.text = track.title
        binding?.txtAuthor?.text = track.author.username
        setupActionButtons()
    }

    private fun updateActionButton(isPlaying: Boolean) {
        if (isPlaying)
            binding?.btnAction?.setImageResource(R.drawable.ic_pause)
        else
            binding?.btnAction?.setImageResource(R.drawable.ic_play)
    }



}