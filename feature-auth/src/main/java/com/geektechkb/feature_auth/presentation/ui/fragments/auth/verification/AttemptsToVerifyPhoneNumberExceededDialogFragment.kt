package com.geektechkb.feature_auth.presentation.ui.fragments.auth.verification

import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseDialogFragmentWithoutViewModel
import com.geektechkb.feature_auth.R
import com.geektechkb.feature_auth.databinding.FragmentAttemptsToVerifyPhoneNumberExceededDialogBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AttemptsToVerifyPhoneNumberExceededDialogFragment :
    BaseDialogFragmentWithoutViewModel<FragmentAttemptsToVerifyPhoneNumberExceededDialogBinding>(R.layout.fragment_attempts_to_verify_phone_number_exceeded_dialog) {
    override val binding by viewBinding(FragmentAttemptsToVerifyPhoneNumberExceededDialogBinding::bind)
    override fun setupListeners() {
        binding.tvContinue.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}