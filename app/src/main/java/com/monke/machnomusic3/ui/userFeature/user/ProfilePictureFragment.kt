package com.monke.machnomusic3.ui.userFeature.user

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
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
import com.bumptech.glide.Glide
import com.monke.machnomusic3.R
import com.monke.machnomusic3.data.files.IMAGE_FILES
import com.monke.machnomusic3.databinding.FragmentProfileBinding
import com.monke.machnomusic3.databinding.FragmentProfilePictureBinding
import com.monke.machnomusic3.databinding.FragmentUserBinding
import com.monke.machnomusic3.main.activity.MainActivity
import com.monke.machnomusic3.ui.components.LoadingDialog
import com.monke.machnomusic3.ui.uiModels.UiState
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfilePictureFragment : Fragment() {

    companion object {
        const val PICK_IMAGE_CODE = 0
    }

    @Inject
    lateinit var factory: ProfilePictureViewModel.Factory
    private val viewModel: ProfilePictureViewModel by viewModels { factory }

    private var binding: FragmentProfilePictureBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfilePictureBinding.inflate(inflater, container, false)
        (activity as? MainActivity)?.mainComponent?.inject(this)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUploadPhotoButton()
        setupProfilePicture()
        setupSaveButton()
        collectUiState()

    }

    private fun setupProfilePicture() {
        val pictureView = binding?.imgProfilePic ?: return
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.profilePicture.collect { uri ->
                        if (uri == null) return@collect
                        Glide
                            .with(this@ProfilePictureFragment)
                            .load(uri)
                            .into(pictureView)
                    }
                }
                launch {
                    viewModel.pictureUrl.collect { url ->
                        if (url == null || viewModel.profilePicture.value != null) return@collect
                        Glide
                            .with(this@ProfilePictureFragment)
                            .load(url)
                            .into(pictureView)
                    }
                }
            }
        }
    }

    private fun setupUploadPhotoButton() {
        binding?.btnPickFile?.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = IMAGE_FILES
            startActivityForResult(intent, PICK_IMAGE_CODE)
        }
    }

    private fun setupSaveButton() {
        binding?.btnSave?.setOnClickListener {
            viewModel.save()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val uri = data?.data
        if (resultCode != AppCompatActivity.RESULT_OK || uri == null)
            return
        if (requestCode == PICK_IMAGE_CODE) {
            viewModel.setProfilePicture(uri)
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