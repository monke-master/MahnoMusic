package com.monke.machnomusic3.ui.signUpFeature

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.monke.machnomusic3.main.MainActivity
import com.monke.machnomusic3.R
import com.monke.machnomusic3.databinding.FragmentPasswordBinding
import kotlinx.coroutines.launch
import javax.inject.Inject

class PasswordFragment : Fragment() {


    @Inject
    lateinit var factory: PasswordViewModel.Factory
    private val viewModel: PasswordViewModel by viewModels { factory }
    private var binding: FragmentPasswordBinding? = null
    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPasswordBinding.inflate(inflater, container, false)
        (activity as? MainActivity)?.loginComponent()?.inject(this)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        setupPasswordEditText()
        setupConfirmedPasswordEditText()
        setupNextButton()
    }

    private fun setupPasswordEditText() {
        binding?.editTextPassword?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(text: Editable?) {
                text?.let { viewModel.setPassword(text.toString()) }
            }
        })
    }

    private fun setupConfirmedPasswordEditText() {
        binding?.editTextConfirmedPassword?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(text: Editable?) {
                text?.let { viewModel.setRepeatedPassword(text.toString()) }
            }
        })
    }

    private fun setupNextButton() {
        val signUpBtn = binding?.btnNext
        signUpBtn?.setOnClickListener {
            viewModel.savePassword()
            navController.navigate(R.id.action_passwordFragment_to_mainFragment)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isPasswordValid.collect { isValid -> signUpBtn?.isEnabled = isValid }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}