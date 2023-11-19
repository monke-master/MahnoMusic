package com.monke.machnomusic3.ui.musicFeature.track

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.monke.machnomusic3.data.files.AUDIO_FILES
import com.monke.machnomusic3.databinding.FragmentUploadTrackBinding
import com.monke.machnomusic3.main.activity.MainActivity
import javax.inject.Inject

class UploadTrackFragment : Fragment() {

    companion object {

        const val PICK_AUDIO_CODE = 1

    }

    @Inject
    lateinit var factory: UploadTrackViewModel.Factory
    private val viewModel: UploadTrackViewModel by viewModels { factory }
    private var binding: FragmentUploadTrackBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUploadTrackBinding.inflate(inflater, container, false)
        (activity as? MainActivity)?.mainComponent?.inject(this)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val intent = Intent()
        intent.type = AUDIO_FILES
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_AUDIO_CODE)

        setupTitleEditText()
    }


    private fun setupTitleEditText() {
        binding?.editTextTitle?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(text: Editable?) {
                viewModel.setTrackTitle(text?.toString() ?: "")
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_AUDIO_CODE) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                viewModel.setTrackUri(data?.data)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }



}