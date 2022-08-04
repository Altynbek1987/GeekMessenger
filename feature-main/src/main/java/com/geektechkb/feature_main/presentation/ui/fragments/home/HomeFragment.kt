package com.geektechkb.feature_main.presentation.ui.fragments.home

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseFragment
import com.geektechkb.core.extensions.directionsSafeNavigation
import com.geektechkb.core.utils.CheckInternet
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.databinding.FragmentHomeBinding
import com.geektechkb.feature_main.presentation.ui.adapters.UsersAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(
    R.layout.fragment_home
) {
    override val binding by viewBinding(FragmentHomeBinding::bind)
    override val galleryViewModel by viewModels<HomeViewModel>()
    private val usersAdapter = UsersAdapter(this::onItemClick)
    private lateinit var cld: CheckInternet

    override fun assembleViews() {
        binding.recyclerview.adapter = usersAdapter
        binding.recyclerview.layoutManager = LinearLayoutManager(context)
    }

    override fun setupListeners() {
        checkInternet()
    }

    private fun checkInternet() {
        activity?.application?.let {
            cld = CheckInternet(it)
        }
        cld.observe(viewLifecycleOwner) {
            if (it) {
                Log.e("connection", it.toString())
            } else {
                findNavController().navigate(R.id.action_homeFragment_to_connectionCheckFragment)
            }
        }
    }

    override fun launchObservers() {
        subscribeToUsers()
    }

    private fun subscribeToUsers() {
        galleryViewModel.fetchPagedUsers().spectatePaging(success = {
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
}
