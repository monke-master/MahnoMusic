package com.monke.machnomusic3.ui.signUpFeature.email

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.monke.machnomusic3.databinding.DialogConfirmEmailBinding
import kotlinx.coroutines.launch

class ConfirmEmailDialog: DialogFragment() {

    private val viewModel: EmailViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )
    private var binding: DialogConfirmEmailBinding? = null

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogConfirmEmailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.emailConfirmed.collect { isConfirmed ->
//                    if (isConfirmed)
//                        dismiss()
//                }
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        (parentFragment as DialogInterface.OnDismissListener).onDismiss(dialog)
    }


}