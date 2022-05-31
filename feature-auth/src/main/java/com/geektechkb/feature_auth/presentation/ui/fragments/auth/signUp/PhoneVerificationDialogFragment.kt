package com.geektechkb.feature_auth.presentation.ui.fragments.auth.signUp

import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseDialogFragment
import com.geektechkb.feature_auth.R
import com.geektechkb.feature_auth.databinding.FragmentPhoneVerificationDialogBinding
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.timplifier.firebaseauthenticationtest.presentation.extensions.directionsSafeNavigation
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class PhoneVerificationDialogFragment :
    BaseDialogFragment<FragmentPhoneVerificationDialogBinding, SignUpViewModel>(R.layout.fragment_phone_verification_dialog) {
    override val binding by viewBinding(FragmentPhoneVerificationDialogBinding::bind)
    override val viewModel: SignUpViewModel by hiltNavGraphViewModels(R.id.authorization_graph)
    private var smsCode: String? = null
    private val args: PhoneVerificationDialogFragmentArgs by navArgs()
    override fun assembleViews() {
        binding.tvEnteredNumber.text = args.inputPhoneNumber
    }


    override fun setupListeners() {
        returnToPhoneEditor()
        proceedToPhoneVerification()
    }


    private fun returnToPhoneEditor() {
        binding.tvEditPhoneNumber.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun proceedToPhoneVerification() {
        binding.tvContinue.setOnClickListener {
            startPhoneNumberVerification()
            findNavController().directionsSafeNavigation(
                PhoneVerificationDialogFragmentDirections.actionPhoneVerificationDialogFragmentToVerifyAuthenticationFragment(
                    args.inputPhoneNumber
                )
            )
        }
    }

    private fun startPhoneNumberVerification() {
        viewModel.setRussianLanguageCode()
        val options = PhoneAuthOptions.newBuilder(viewModel.firebaseAuth)
            .setPhoneNumber(args.inputPhoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(viewModel.provideCallback(requireContext()))
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }

}