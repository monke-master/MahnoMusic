package com.monke.machnomusic3.ui.musicFeature.album

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.monke.machnomusic3.R

class UploadAlbumFragment : Fragment() {

    companion object {
        fun newInstance() = UploadAlbumFragment()
    }

    private lateinit var viewModel: UploadAlbumViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_upload_album, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UploadAlbumViewModel::class.java)
        // TODO: Use the ViewModel
    }

}