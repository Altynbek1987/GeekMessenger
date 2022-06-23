package com.geektechkb.feature_main.presentation.ui.fragments.home

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseFragment
import com.geektechkb.core.extensions.directionsSafeNavigation
import com.geektechkb.core.extensions.formatCurrentUserTime
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.databinding.FragmentHomeBinding
import com.geektechkb.feature_main.presentation.ui.adapters.UsersAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(
    R.layout.fragment_home
) {
    override val binding by viewBinding(FragmentHomeBinding::bind)
    override val viewModel: HomeViewModel by viewModels()
    private val usersAdapter = UsersAdapter(this::onItemClick)


    override fun assembleViews() {
        binding.recyclerview.adapter = usersAdapter
        binding.recyclerview.layoutManager = LinearLayoutManager(context)
    }

    override fun launchObservers() {
        subscribeToUsers()


    }

    private fun subscribeToUsers() {
        viewModel.fetchPagedUsers().spectatePaging(success = {
            usersAdapter.submitData(it)
        })
    }


    private fun onItemClick(phoneNumber: String?) {
        findNavController().directionsSafeNavigation(
            HomeFragmentDirections.actionHomeFragmentToChatFragment(
                phoneNumber
            )
        )
    }

    override fun onStart() {
        super.onStart()

        viewModel.updateUserStatus("В сети")
    }

    override fun onStop() {
        super.onStop()
        viewModel.updateUserStatus(formatCurrentUserTime("HH:mm"))
    }
}