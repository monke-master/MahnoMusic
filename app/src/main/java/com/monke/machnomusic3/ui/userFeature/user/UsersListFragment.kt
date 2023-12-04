package com.monke.machnomusic3.ui.userFeature.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.monke.machnomusic3.R
import com.monke.machnomusic3.databinding.FragmentUsersListBinding
import com.monke.machnomusic3.main.activity.MainActivity
import com.monke.machnomusic3.ui.userFeature.recycler.UserRWAdapter
import kotlinx.coroutines.launch
import javax.inject.Inject

class UsersListFragment : Fragment() {

    companion object {

        const val BUNDLE_KEY_ID_LIST = "com.monke.machnomusic3.ui.userFeature.user.idList"

    }


    @Inject
    lateinit var factory: UsersListViewModel.Factory
    private val viewModel: UsersListViewModel by viewModels { factory }

    private var binding: FragmentUsersListBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUsersListBinding.inflate(inflater, container, false)
        (activity as? MainActivity)?.mainComponent?.inject(this)
        arguments?.getStringArray(BUNDLE_KEY_ID_LIST)?.let {
            viewModel.loadUsers(it.toList())
        }

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUserRecycler()

    }

    private fun setupUserRecycler() {
        val usersAdapter = UserRWAdapter(
            onItemClicked = { userId ->
                findNavController().navigate(
                    R.id.action_usersListFragment_to_userFragment,
                    bundleOf(
                        UserFragment.BUNDLE_KEY_USER_ID to userId
                    )
                )
            }
        )

        binding?.recyclerUsers?.adapter = usersAdapter
        binding?.recyclerUsers?.layoutManager = LinearLayoutManager(
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

}