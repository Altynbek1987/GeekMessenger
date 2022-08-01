package com.geektechkb.feature_auth.presentation.ui.fragments.auth.signUp

import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseFragment
import com.geektechkb.core.extensions.directionsSafeNavigation
import com.geektechkb.feature_auth.R
import com.geektechkb.feature_auth.databinding.FragmentSignUpBinding
import com.google.android.material.textfield.TextInputLayout
import com.santalu.maskara.MaskChangedListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SignUpFragment :
    BaseFragment<FragmentSignUpBinding, SignUpViewModel>(R.layout.fragment_sign_up) {
    override val binding by viewBinding(FragmentSignUpBinding::bind)
    override val viewModel: SignUpViewModel by hiltNavGraphViewModels(R.id.authorization_graph)
    private var phoneNumberLength: Int? = null
    private lateinit var maskChangedListener: MaskChangedListener


    override fun setupListeners() {
        openPhoneNumberVerificationDialog()
    }


    private fun openPhoneNumberVerificationDialog() {
        binding.btnContinue.setOnClickListener {
            findNavController().directionsSafeNavigation(
                SignUpFragmentDirections.actionSignUpFragmentToPhoneVerificationDialogFragment(
                    "${binding.etPhone.text?.trim()}"
                )
            )
        }

    }
}


private fun TextInputLayout.setFixedError(errorTxt: CharSequence?) {
    if (error != errorTxt) {
        error = errorTxt
    }
}

//    private fun TextInputEditText.addMaskChangeListener() {
//        maskChangedListener = MaskChangedListener(
//            Mask(
//                value = " ___ ___ ___",
//                character = '_',
//                style = MaskStyle.PERSISTENT
//            )
//        )
//        addTextChangedListener(maskChangedListener)
//    }
//}