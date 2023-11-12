package com.monke.machnomusic3.ui.musicFeature.musicPlayer

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.monke.machnomusic3.databinding.FragmentMiniPlayerBinding
import com.monke.machnomusic3.main.MainActivity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class MiniPlayerFragment : Fragment() {


    @Inject
    lateinit var factory: MiniPlayerViewModel.Factory
    private val viewModel: MiniPlayerViewModel by viewModels { factory }

    private var binding: FragmentMiniPlayerBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMiniPlayerBinding.inflate(inflater, container, false)
        (activity as? MainActivity)?.mainComponent()?.inject(this)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.track.collect {
                    Log.d("MiniPla", "yer")
                }
            }
        }
    }


}