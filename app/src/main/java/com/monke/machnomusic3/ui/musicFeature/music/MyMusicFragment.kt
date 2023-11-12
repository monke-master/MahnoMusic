package com.monke.machnomusic3.ui.musicFeature.music

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.monke.machnomusic3.databinding.FragmentMusicLibraryBinding
import com.monke.machnomusic3.databinding.FragmentMyMusicBinding
import com.monke.machnomusic3.domain.model.mockedTracks
import com.monke.machnomusic3.main.MainActivity
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
        (activity as? MainActivity)?.mainComponent()?.inject(this)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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