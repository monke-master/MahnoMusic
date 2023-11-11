package com.monke.machnomusic3.ui.signUpFeature

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.storage
import com.monke.machnomusic3.MainActivity
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

        val ref = Firebase.storage.getReferenceFromUrl("gs://rockit-e8345.appspot.com/music/07b16a5a-86dc-4ee2-b73e-ea3e48412bd6")
        ref.downloadUrl.addOnSuccessListener {
            Log.d("dkdkd", "Berkish, sir!")
            (activity as MainActivity).playTrack(it.toString())
        }


        binding?.btnSignUp?.setOnClickListener {
            it.findNavController().navigate(R.id.action_startFragment_to_emailFragment)
        }
    }

}