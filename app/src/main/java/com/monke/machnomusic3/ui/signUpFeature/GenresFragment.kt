package com.monke.machnomusic3.ui.signUpFeature

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.monke.machnomusic3.R
import com.monke.machnomusic3.databinding.FragmentEmailBinding
import com.monke.machnomusic3.databinding.FragmentGenresBinding

class GenresFragment : Fragment() {

    private var binding: FragmentGenresBinding? = null

    private lateinit var viewModel: GenresViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGenresBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.btnSignUp?.setOnClickListener {
            it.findNavController().navigate(R.id.action_genresFragment_to_mainFragment)
        }
    }

}