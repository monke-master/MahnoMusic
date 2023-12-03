package com.monke.machnomusic3.ui.mainFeature

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.monke.machnomusic3.R
import com.monke.machnomusic3.databinding.FragmentEmailBinding
import com.monke.machnomusic3.databinding.FragmentMainBinding
import com.monke.machnomusic3.domain.model.MusicState
import com.monke.machnomusic3.main.activity.MainActivity
import com.monke.machnomusic3.ui.musicFeature.album.AlbumViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainFragment : Fragment() {

    @Inject
    lateinit var factory: MainViewModel.Factory
    private val viewModel: MainViewModel by viewModels { factory }

    private var binding: FragmentMainBinding? = null
    lateinit var mainNavController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        (activity as? MainActivity)?.mainComponent?.inject(this)
        return binding?.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment = childFragmentManager.findFragmentById(R.id.fragment_container)

        if (navHostFragment != null) {
            val navController = (navHostFragment as NavHostFragment).navController
            binding?.bottomNavigationView?.setupWithNavController(navController)
        }
        mainNavController = view.findNavController()

        setupMiniPlayerFragment()

    }

    private fun setupMiniPlayerFragment() {
        binding?.fragmentMiniPlayer?.setOnClickListener {
            it.findNavController().navigate(R.id.action_mainFragment_to_musicPlayerFragment)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.musicState.collect { state ->
                    when (state) {
                        MusicState.Start -> binding?.fragmentMiniPlayer?.visibility = View.VISIBLE
                        else -> {}
                    }
                }
            }
        }

    }

}