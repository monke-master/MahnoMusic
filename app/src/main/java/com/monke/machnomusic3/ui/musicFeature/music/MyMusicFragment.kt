package com.monke.machnomusic3.ui.musicFeature.music

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.monke.machnomusic3.R
import com.monke.machnomusic3.databinding.FragmentMyMusicBinding
import com.monke.machnomusic3.domain.model.mockedTracks
import com.monke.machnomusic3.main.activity.MainActivity
import com.monke.machnomusic3.ui.mainFeature.MainFragment
import com.monke.machnomusic3.ui.musicFeature.adapters.TrackRWAdapter
import javax.inject.Inject

class MyMusicFragment : Fragment() {


    @Inject
    lateinit var factory: MyMusicViewModel.Factory
    private val viewModel: MyMusicViewModel by viewModels { factory }

    private var binding: FragmentMyMusicBinding? = null


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
        setupUploadTrackButton()
        setupTracksRecyclerList()
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
                viewModel.playTrackList(mockedTracks.toList(), index)
            }
        )
        tracksAdapter.tracks = mockedTracks.toList()
        binding?.recyclerTracks?.adapter = tracksAdapter
        binding?.recyclerTracks?.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
    }

}