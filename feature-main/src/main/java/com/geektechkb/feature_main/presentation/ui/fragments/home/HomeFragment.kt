package com.geektechkb.feature_main.presentation.ui.fragments.home

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseFragment
import com.geektechkb.core.utils.CheckInternet
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(
    R.layout.fragment_home
) {
    override val binding by viewBinding(FragmentHomeBinding::bind)
    override val viewModel by viewModels<HomeViewModel>()
    private lateinit var cld: CheckInternet

    override fun setupListeners() {
        checkInternet()
    }

    private fun checkInternet() {
        activity?.application?.let {
            cld = CheckInternet(it)
        }
        cld.observe(viewLifecycleOwner) {
            if (it) {
                Log.e("connection",it.toString())
            } else {
                findNavController().navigate(R.id.action_homeFragment_to_connectionCheckFragment)
            }
        }
    }
}
