package com.monke.machnomusic3.ui.musicFeature.music

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.monke.machnomusic3.R

class MusicLibraryFragment : Fragment() {

    companion object {
        fun newInstance() = MusicLibraryFragment()
    }

    private lateinit var viewModel: MusicLibraryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_music_library, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MusicLibraryViewModel::class.java)
        // TODO: Use the ViewModel
    }

}