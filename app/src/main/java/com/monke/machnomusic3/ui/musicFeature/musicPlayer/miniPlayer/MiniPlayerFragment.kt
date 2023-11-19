package com.monke.machnomusic3.ui.musicFeature.musicPlayer.miniPlayer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.monke.machnomusic3.R
import com.monke.machnomusic3.databinding.FragmentMiniPlayerBinding
import com.monke.machnomusic3.domain.model.MusicState
import com.monke.machnomusic3.domain.model.Track
import com.monke.machnomusic3.main.activity.MainActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class MiniPlayerFragment : Fragment() {


    @Inject
    lateinit var factory: MiniPlayerViewModel.Factory
    private val viewModel: MiniPlayerViewModel by viewModels { factory }

    private var binding: FragmentMiniPlayerBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMiniPlayerBinding.inflate(inflater, container, false)
        (activity as? MainActivity)?.mainComponent()?.inject(this)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.musicState.collect {state ->
                    when (state) {
                        MusicState.Pause -> {
                            updateActionButton(isPlaying = false)
                        }
                        MusicState.Resume -> {
                            updateActionButton(isPlaying = true)
                        }
                        MusicState.Start -> {
                            viewModel.track.first()?.let { setTrackInfo(it) }
                            updateActionButton(isPlaying = true)
                        }
                        MusicState.Stop -> {

                        }
                    }
                }
            }
        }
    }

    private fun setupActionButton() {
        binding?.btnAction?.setOnClickListener {
            viewModel.updateState()
        }
    }

    private fun setTrackInfo(track: Track) {
        binding?.txtTitle?.text = track.title
        binding?.txtAuthor?.text = track.author.username
        setupActionButton()
    }

    private fun updateActionButton(isPlaying: Boolean) {
        if (isPlaying)
            binding?.btnAction?.setImageResource(R.drawable.ic_pause)
        else
            binding?.btnAction?.setImageResource(R.drawable.ic_play)
    }


}