package com.monke.machnomusic3.ui.musicFeature.music.search

import androidx.lifecycle.ViewModelProvider
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
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.monke.machnomusic3.R
import com.monke.machnomusic3.databinding.FragmentMyMusicBinding
import com.monke.machnomusic3.databinding.FragmentSearchMusicBinding
import com.monke.machnomusic3.main.activity.MainActivity
import com.monke.machnomusic3.ui.musicFeature.adapters.TrackRWAdapter
import com.monke.machnomusic3.ui.musicFeature.music.myMusic.MyMusicViewModel
import com.monke.machnomusic3.ui.uiModels.UiState
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchMusicFragment : Fragment() {

    @Inject
    lateinit var factory: SearchMusicViewModel.Factory
    private val viewModel: SearchMusicViewModel by viewModels { factory }

    private var binding: FragmentSearchMusicBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchMusicBinding.inflate(inflater, container, false)
        (activity as? MainActivity)?.mainComponent?.inject(this)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectUiState()
        setupSearchEditText()
        setupTracksRecycler()
    }


    private fun setupTracksRecycler() {
        val tracksAdapter = TrackRWAdapter(
            onItemClicked = { index ->
                //viewModel.playTrackList(viewModel.tracksList.value.map { it.track }, index)
            }
        )

        binding?.recyclerTracks?.adapter = tracksAdapter
        binding?.recyclerTracks?.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.tracksList.collect {
                    tracksAdapter.tracksList = it
                }
            }
        }
    }



    private fun setupSearchEditText() {
        binding?.editTextTitle?.setText(viewModel.query)
        binding?.editTextTitle?.addTextChangedListener(object : TextWatcher {
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