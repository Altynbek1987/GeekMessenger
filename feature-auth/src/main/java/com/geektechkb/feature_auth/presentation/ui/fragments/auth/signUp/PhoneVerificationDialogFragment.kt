package com.geektechkb.feature_auth.presentation.ui.fragments.auth.signUp

import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseDialogFragment
import com.geektechkb.core.extensions.directionsSafeNavigation
import com.geektechkb.core.extensions.showShortDurationSnackbar
import com.geektechkb.feature_auth.R
import com.geektechkb.feature_auth.databinding.FragmentPhoneVerificationDialogBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PhoneVerificationDialogFragment :
    BaseDialogFragment<FragmentPhoneVerificationDialogBinding, SignUpViewModel>(R.layout.fragment_phone_verification_dialog) {
    override val binding by viewBinding(FragmentPhoneVerificationDialogBinding::bind)
    override val viewModel: SignUpViewModel by hiltNavGraphViewModels(R.id.authorization_graph)
    private val args: PhoneVerificationDialogFragmentArgs by navArgs()
    override fun assembleViews() {
        binding.tvEnteredNumber.text =
            "${args.inputPhoneNumber.substring(0, 4)} ${
                args.inputPhoneNumber.substringAfter("+996")
                    .chunked(3).joinToString(" ")
            }"
    }


    override fun setupListeners() {
        returnToPhoneEditor()
        proceedToPhoneVerification()
    }


    private fun returnToPhoneEditor() {
        binding.tvEditPhoneNumber.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.imEditPhoneNumber.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun proceedToPhoneVerification() {
        binding.tvContinue.setOnClickListener {
            viewModel.startPhoneNumberVerification(
                viewModel.firebaseAuth,
                args.inputPhoneNumber,
                requireActivity(),
                viewModel.provideCallbacks(
                    authenticationSucceeded =
                    {
                        showShortDurationSnackbar("Вы успешно аутентифицировались")
                    },
                    authInvalidCredentialsError = {
                        showShortDurationSnackbar("The phone number you entered was wrong")
                    },
                    tooManyRequestsError = {
                        showShortDurationSnackbar(
                            "Looks like you have used all of the requests available"
                        )
                    })
            )
            findNavController().directionsSafeNavigation(
                PhoneVerificationDialogFragmentDirections.actionPhoneVerificationDialogFragmentToVerifyAuthenticationFragment(
                    args.inputPhoneNumber


                )
            )
        }
    }
}