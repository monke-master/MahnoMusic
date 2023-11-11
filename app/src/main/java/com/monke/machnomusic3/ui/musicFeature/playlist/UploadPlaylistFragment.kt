package com.monke.machnomusic3.ui.musicFeature.playlist

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.monke.machnomusic3.R

class UploadPlaylistFragment : Fragment() {

    companion object {
        fun newInstance() = UploadPlaylistFragment()
    }

    private lateinit var viewModel: UploadPlaylistViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_upload_playlist, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UploadPlaylistViewModel::class.java)
        // TODO: Use the ViewModel
    }

}