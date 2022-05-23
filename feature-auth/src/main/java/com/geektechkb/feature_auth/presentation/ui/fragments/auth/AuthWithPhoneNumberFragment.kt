package com.geektechkb.feature_auth.presentation.ui.fragments.auth

import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseFragment
import com.geektechkb.core.extensions.navigateSafely
import com.geektechkb.feature_auth.R
import com.geektechkb.feature_auth.databinding.FragmentAuthWithPhoneNumberBinding
import com.geektechkb.feature_auth.presentation.ui.extensions.mainNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthWithPhoneNumberFragment :
    BaseFragment<FragmentAuthWithPhoneNumberBinding, AuthWithPhoneNumberViewModel>(R.layout.fragment_auth_with_phone_number) {
    override val binding by viewBinding(FragmentAuthWithPhoneNumberBinding::bind)
    override val viewModel by viewModels<AuthWithPhoneNumberViewModel>()


    override fun setupListeners() {
        binding.buttonSignIn.setOnClickListener {
            viewModel.isAuthorizeTrue()
            mainNavController().navigateSafely(R.id.action_authWithPhoneNumberFragment_to_mainFlowFragment)
        }
    }
}