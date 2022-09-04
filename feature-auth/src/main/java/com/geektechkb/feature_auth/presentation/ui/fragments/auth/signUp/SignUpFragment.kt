package com.geektechkb.feature_auth.presentation.ui.fragments.auth.signUp

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseFragment
import com.geektechkb.core.extensions.addTextChangedListenerAnonymously
import com.geektechkb.core.extensions.directionsSafeNavigation
import com.geektechkb.feature_auth.R
import com.geektechkb.feature_auth.data.local.preferences.AuthorizePreferences
import com.geektechkb.feature_auth.databinding.FragmentSignUpBinding
import com.vicmikhailau.maskededittext.MaskedFormatter
import com.vicmikhailau.maskededittext.MaskedWatcher
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SignUpFragment :
    BaseFragment<FragmentSignUpBinding, SignUpViewModel>(R.layout.fragment_sign_up) {
    @Inject
    lateinit var authorizePreferences: AuthorizePreferences

    override val binding by viewBinding(FragmentSignUpBinding::bind)
    override val viewModel by viewModels<SignUpViewModel>()
    private val maskFormatter = MaskedFormatter("### ### ###")

    override fun initialize() {
        addMaskToThePhoneNumberEditText()
        disableHelperText()
    }


    private fun addMaskToThePhoneNumberEditText() {
        binding.etPhone.addTextChangedListener(MaskedWatcher(maskFormatter, binding.etPhone))
    }

    private fun disableHelperText() {
    }


    override fun setupListeners() {
        openPhoneNumberVerificationDialog()
    }


    private fun openPhoneNumberVerificationDialog() {
        binding.btnContinue.setOnClickListener {
            if (binding.etPhone.text?.length != 11) {
                binding.tlPhone.isErrorEnabled = true
                binding.tlPhone.error = getString(R.string.phone_number_must_consist_of_9_digits)
            } else

                findNavController().directionsSafeNavigation(
                    SignUpFragmentDirections.actionSignUpFragmentToPhoneVerificationDialogFragment(
                        binding.tvCountryPhoneCode.text.toString() + maskFormatter.formatString(
                            binding.etPhone.text?.toString().toString()
                        )?.unMaskedString
                    )
                )
            binding.etPhone.addTextChangedListenerAnonymously(doSomethingOnTextChanged = {
                binding.tlPhone.isErrorEnabled = false
            })
        }

    }
}


