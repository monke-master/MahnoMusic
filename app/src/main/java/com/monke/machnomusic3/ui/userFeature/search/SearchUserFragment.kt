package com.monke.machnomusic3.ui.userFeature.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.monke.machnomusic3.databinding.FragmentSearchUserBinding
import com.monke.machnomusic3.main.activity.MainActivity
import com.monke.machnomusic3.ui.uiModels.UiState
import com.monke.machnomusic3.ui.userFeature.recycler.UserRWAdapter
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchUserFragment : Fragment() {

    @Inject
    lateinit var factory: SearchUserViewModel.Factory
    private val viewModel: SearchUserViewModel by viewModels { factory }

    private var binding: FragmentSearchUserBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchUserBinding.inflate(inflater, container, false)
        (activity as? MainActivity)?.mainComponent?.inject(this)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectUiState()
        setupSearchEditText()
        setupUserRecycler()
    }


    private fun setupUserRecycler() {
        val usersAdapter = UserRWAdapter(
            onItemClicked = { index ->
                //viewModel.playTrackList(viewModel.tracksList.value.map { it.track }, index)
            }
        )

        binding?.recyclerTracks?.adapter = usersAdapter
        binding?.recyclerTracks?.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.usersList.collect {
                    usersAdapter.usersList = it
                }
            }
        }
    }



    private fun setupSearchEditText() {
        binding?.editTextSearch?.setText(viewModel.query)
        binding?.editTextSearch?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(text: Editable?) {
                text?.let { viewModel.search(text.toString()) }
            }
        })
    }

    private fun collectUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        UiState.Loading -> {}
                        is UiState.Error ->
                            Toast.makeText(
                                requireContext(),
                                state.exception.message,
                                Toast.LENGTH_SHORT).
                            show()
                        is UiState.Success -> {}
                        else -> {}
                    }
                }
            }
        }
    }

}