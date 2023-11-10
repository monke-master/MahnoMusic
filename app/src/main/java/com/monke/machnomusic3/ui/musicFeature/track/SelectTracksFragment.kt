package com.monke.machnomusic3.ui.musicFeature.track

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.monke.machnomusic3.R

class SelectTracksFragment : Fragment() {

    companion object {
        fun newInstance() = SelectTracksFragment()
    }

    private lateinit var viewModel: SelectTracksViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_select_tracks, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SelectTracksViewModel::class.java)
        // TODO: Use the ViewModel
    }

}