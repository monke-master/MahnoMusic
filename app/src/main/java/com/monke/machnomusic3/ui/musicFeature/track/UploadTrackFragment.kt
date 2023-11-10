package com.monke.machnomusic3.ui.musicFeature.track

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.monke.machnomusic3.R

class UploadTrackFragment : Fragment() {

    companion object {
        fun newInstance() = UploadTrackFragment()
    }

    private lateinit var viewModel: UploadTrackViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_upload_track, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UploadTrackViewModel::class.java)
        // TODO: Use the ViewModel
    }

}