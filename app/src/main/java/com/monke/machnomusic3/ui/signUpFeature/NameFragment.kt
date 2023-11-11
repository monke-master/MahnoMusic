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
import com.monke.machnomusic3.databinding.FragmentNameBinding

class NameFragment : Fragment() {

    private var binding: FragmentNameBinding? = null

    private lateinit var viewModel: NameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNameBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.btnNext?.setOnClickListener {
            it.findNavController().navigate(R.id.action_nameFragment_to_passwordFragment)
        }
    }

}