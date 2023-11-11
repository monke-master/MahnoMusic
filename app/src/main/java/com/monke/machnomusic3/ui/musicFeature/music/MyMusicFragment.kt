package com.monke.machnomusic3.ui.musicFeature.music

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.monke.machnomusic3.R
import com.monke.machnomusic3.databinding.FragmentMyMusicBinding
import com.monke.machnomusic3.domain.models.Track
import com.monke.machnomusic3.domain.models.User
import com.monke.machnomusic3.domain.models.mockedTracks
import com.monke.machnomusic3.ui.musicFeature.adapters.TrackRWAdapter

class MyMusicFragment : Fragment() {


    private var binding: FragmentMyMusicBinding? = null
    private lateinit var viewModel: MyMusicViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyMusicBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tracksAdapter = TrackRWAdapter()
        tracksAdapter.tracks = mockedTracks.toList()
        binding?.recyclerTracks?.adapter = tracksAdapter
        binding?.recyclerTracks?.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )

    }

}