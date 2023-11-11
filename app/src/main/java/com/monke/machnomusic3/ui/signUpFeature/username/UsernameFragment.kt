package com.monke.machnomusic3.ui.signUpFeature.username

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
import androidx.navigation.findNavController
import com.monke.machnomusic3.MainActivity
import com.monke.machnomusic3.R
import com.monke.machnomusic3.databinding.FragmentUsernameBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class UsernameFragment : Fragment() {

    @Inject
    lateinit var factory: UsernameViewModel.Factory
    private var binding: FragmentUsernameBinding? = null

    private val viewModel: UsernameViewModel by viewModels { factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUsernameBinding.inflate(inflater, container, false)
        (activity as? MainActivity)?.loginComponent()?.inject(this)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupNextButton()
        setupUsernameEditText()
        setupLoginEditText()
    }

    private fun setupNextButton() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.username.onEach { username ->
                    binding?.btnNext?.isEnabled =
                        username.isNotEmpty() && viewModel.login.value.isNotEmpty()
                }.launchIn(this)

                viewModel.login.onEach { login ->
                    binding?.btnNext?.isEnabled =
                        viewModel.username.value.isNotEmpty() && login.isNotEmpty()
                }.launchIn(this)

            }
        }
        binding?.btnNext?.setOnClickListener {
            viewModel.saveData()
            it.findNavController().navigate(R.id.action_nameFragment_to_passwordFragment)
        }
    }


    private fun setupUsernameEditText() {
        binding?.editTextUsername?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(text: Editable?) {
                text?.let { viewModel.setUsername(text.toString()) }
            }
        })
    }

    private fun setupLoginEditText() {
        binding?.editTextLogin?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(text: Editable?) {
                text?.let { viewModel.setLogin(text.toString()) }
            }
        })
    }

}