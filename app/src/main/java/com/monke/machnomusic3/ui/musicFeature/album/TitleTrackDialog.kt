package com.monke.machnomusic3.ui.musicFeature.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.monke.machnomusic3.databinding.DialogTitleTrackBinding


class TitleTrackDialog : DialogFragment() {

    private val viewModel: UploadAlbumViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )
    private var binding: DialogTitleTrackBinding? = null

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogTitleTrackBinding.inflate(inflater, container, false)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.btnUpload?.setOnClickListener {
            val title = binding?.editTextTitle?.text?.toString() ?: ""
            viewModel.setTrackTitle(title)
            dismiss()
        }
    }

}