package com.monke.machnomusic3.ui.signInFeature

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.monke.machnomusic3.R
import com.monke.machnomusic3.databinding.FragmentSignInBinding
import com.monke.machnomusic3.databinding.FragmentStartBinding

class SignInFragment : Fragment() {

    private var binding: FragmentSignInBinding? = null
    private lateinit var viewModel: SignInViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.btnSignUp?.setOnClickListener {
            it.findNavController().navigate(R.id.action_signInFragment_to_mainFragment)
        }
    }

}