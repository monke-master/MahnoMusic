package com.monke.machnomusic3.ui.mainFeature

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.monke.machnomusic3.R
import com.monke.machnomusic3.databinding.FragmentEmailBinding
import com.monke.machnomusic3.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var binding: FragmentMainBinding? = null
    private lateinit var viewModel: MainViewModel

    lateinit var mainNavController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
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

        binding?.fragmentMiniPlayer?.setOnClickListener {
            it.findNavController().navigate(R.id.action_mainFragment_to_musicPlayerFragment)
        }

        mainNavController = view.findNavController()

    }

}