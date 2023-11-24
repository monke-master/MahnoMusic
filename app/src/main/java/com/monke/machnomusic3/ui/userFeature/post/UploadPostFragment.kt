package com.monke.machnomusic3.ui.userFeature.post

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.monke.machnomusic3.data.files.IMAGE_FILES
import com.monke.machnomusic3.databinding.FragmentUploadPostBinding
import com.monke.machnomusic3.main.activity.MainActivity
import com.monke.machnomusic3.ui.components.LoadingDialog
import com.monke.machnomusic3.ui.uiModels.UiState
import kotlinx.coroutines.launch
import javax.inject.Inject

class UploadPostFragment : Fragment() {


    @Inject
    lateinit var factory: UploadPostViewModel.Factory
    private val viewModel: UploadPostViewModel by viewModels { factory }

    private var binding: FragmentUploadPostBinding? = null

    companion object {

        const val PICK_IMAGE_CODE = 1

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUploadPostBinding.inflate(inflater, container, false)
        (activity as? MainActivity)?.mainComponent?.inject(this)
        setupUploadPhotoBtn()
        setupPhotoRecyclerList()
        collectUiState()
        setupTextEditText()
        setupUploadPostBtn()
        return binding?.root
    }

    private fun setupTextEditText() {
        binding?.editText?.setText(viewModel.text)
        binding?.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(text: Editable?) {
                viewModel.text = text?.toString()
            }

        })
    }




    private fun setupUploadPhotoBtn() {
        binding?.btnUploadPhoto?.setOnClickListener {
            val intent = Intent()
            intent.type = IMAGE_FILES
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, PICK_IMAGE_CODE)
        }
    }

    private fun setupPhotoRecyclerList() {
        val imageAdapter = UploadingImageRWAdapter(
            onItemClicked = {

            }
        )

        binding?.recyclerImages?.adapter = imageAdapter
        binding?.recyclerImages?.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.imagesList.collect {
                    imageAdapter.imagesList = it
                }
            }
        }

    }

    private fun setupUploadPostBtn() {
        binding?.btnUploadPost?.setOnClickListener {
            viewModel.uploadPost()
        }
    }

    private fun setupTracksRecyclerList() {
//        val tracksAdapter = TrackRWAdapter(
//            onItemClicked = { index ->
//                viewModel.playTrackList(viewModel.tracksList.value.map { it.track }, index)
//            }
//        )
//
//        binding?.recyclerTracks?.adapter = tracksAdapter
//        binding?.recyclerTracks?.layoutManager = LinearLayoutManager(
//            context,
//            LinearLayoutManager.VERTICAL,
//            false
//        )
//
//        viewLifecycleOwner.lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.tracksList.collect {
//                    tracksAdapter.tracksList = it
//                }
//            }
//        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val uri = data?.data
        if (resultCode != AppCompatActivity.RESULT_OK || uri == null)
            return

        when (requestCode) {
            PICK_IMAGE_CODE -> viewModel.addImage(uri)
        }
    }


    private fun collectUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        UiState.Loading -> showLoadingDialog()
                        is UiState.Error ->
                            Toast.makeText(
                                requireContext(),
                                state.exception.message,
                                Toast.LENGTH_SHORT).
                            show()
                        is UiState.Success -> {
                            findNavController().popBackStack()
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun showLoadingDialog() {
        val dialog = LoadingDialog()
        dialog.show(parentFragmentManager, dialog.tag)
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    if (state !is UiState.Loading)
                        dialog.dismiss()
                }
            }
        }
    }






}