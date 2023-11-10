package com.monke.machnomusic3.ui.musicFeature

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.monke.machnomusic3.R

class MyMusicFragment : Fragment() {

    companion object {
        fun newInstance() = MyMusicFragment()
    }

    private lateinit var viewModel: MyMusicViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_music, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyMusicViewModel::class.java)
        // TODO: Use the ViewModel
    }

}