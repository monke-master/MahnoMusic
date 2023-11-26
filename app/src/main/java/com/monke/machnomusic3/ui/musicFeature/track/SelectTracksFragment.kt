package com.monke.machnomusic3.ui.musicFeature.track

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.monke.machnomusic3.R
import com.monke.machnomusic3.databinding.FragmentMyMusicBinding
import com.monke.machnomusic3.databinding.FragmentSearchMusicBinding

class SelectTracksFragment : Fragment() {


    private lateinit var viewModel: SelectTracksViewModel
    private var binding: FragmentSearchMusicBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchMusicBinding.inflate(inflater, container, false)



        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.editTextTitleLayout?.requestFocus()
    }

}