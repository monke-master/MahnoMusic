package com.monke.machnomusic3.ui.signUpFeature

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.monke.machnomusic3.R
import com.monke.machnomusic3.databinding.FragmentStartBinding

class StartFragment : Fragment() {


    private lateinit var viewModel: StartViewModel
    private var binding: FragmentStartBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.btnSignUp?.setOnClickListener {
            it.findNavController().navigate(R.id.action_startFragment_to_emailFragment)
        }

        binding?.btnSignIn?.setOnClickListener {
            it.findNavController().navigate(R.id.action_startFragment_to_signInFragment)
        }

        //Firebase.auth.currentUser?.let { view.findNavController().navigate(R.id.action_startFragment_to_mainFragment) }
    }

}