package com.geektechkb.feature_main.presentation.ui.fragments.home

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseFragment
import com.geektechkb.core.extensions.directionsSafeNavigation
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.databinding.FragmentHomeBinding
import com.geektechkb.feature_main.presentation.ui.adapters.UsersAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.log

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
        lifecycleScope.launch{
            viewModel.fetchPagedUsers().collectLatest {
                usersAdapter.submitData(it)
            }
        }
        viewModel.fetch.spectateUiState(success = {
            Log.e("Success", it.toString())
        }, error = {
            Log.e("anime", "Error: ${it.toString()}", )
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