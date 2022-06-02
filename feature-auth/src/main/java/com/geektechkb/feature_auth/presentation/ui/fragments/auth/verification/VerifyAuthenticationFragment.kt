package com.geektechkb.feature_auth.presentation.ui.fragments.auth.verification

import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseFragment
import com.geektechkb.core.extensions.extensions.*
import com.geektechkb.feature_auth.R
import com.geektechkb.feature_auth.databinding.FragmentVerifyAuthenticationBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class VerifyAuthenticationFragment :
    BaseFragment<FragmentVerifyAuthenticationBinding, VerifyAuthenticationViewModel>(R.layout.fragment_verify_authentication) {
    override val binding by viewBinding(FragmentVerifyAuthenticationBinding::bind)
    override val viewModel: VerifyAuthenticationViewModel by viewModels()
    private val args: VerifyAuthenticationFragmentArgs by navArgs()
    private var inputVerificationCode: String = ""
    private var timeInSeconds = 0L
    private var attemptsToVerifyPhoneNumber = 1
    private lateinit var countDownTimer: CountDownTimer
    override fun assembleViews() {
        setPhoneNumberCodeWasSentTo()
        updateCountDownTimer()
        setupCountDownTimer()

    }

    private fun updateCountDownTimer() {
        val minute = (timeInSeconds / 1000) / 60
        val seconds = (timeInSeconds / 1000) % 60
        if (seconds <= 9) {
            "Request the verification code again in 0 $minute:0$seconds".also {
                if (view != null)
                    binding.tvCountDownTimer.text = it
            }
        } else {
            "Request the verification code again in 0$minute:$seconds".also {
                if (view != null) {
                    binding.tvCountDownTimer.text = it
                }
            }
        }
    }

    private fun setPhoneNumberCodeWasSentTo() {
        val phoneNumberVerificationCodeWasSentTo =
            String.format(
                getString(R.string.verification_code_was_sent_on_the_input_number),
                args.phoneNumber
            )
        binding.tvVerificationCodeWasSent.text = phoneNumberVerificationCodeWasSentTo
    }


    override fun setupListeners() {
        returnBackToTheNumberInput()
        addBackspaceListener()
        moveToTheNextDigit()
        enableNumericKeyboardListeners()
        focusOnTheFirstDigit()
        disableEditTextsKeyListener()
        verifyPhoneNumberUsingCode()
        resendVerificationCode()
    }

    private fun resendVerificationCode() {
        binding.tvGetVerificationCode.setOnClickListener {
            binding.tvCountDownTimer.isVisible = true
            binding.tvGetVerificationCode.isVisible = false
            setupCountDownTimer()
            resendVerificationCode(args.phoneNumber, viewModel.getForceResendingToken())
        }
    }

    private fun verifyPhoneNumberUsingCode() {
        binding.apply {
            btnContinue.setOnClickListener {
                inputVerificationCode = etFirstDigit.retrieveInputVerificationCode(
                    etSecondDigit,
                    etThirdDigit,
                    etFourthDigit,
                    etFifthDigit,
                    etSixthDigit
                )
                signInWithPhoneAuthCredential(
                    viewModel.verifyPhoneNumberWithCode(
                        viewModel.getVerificationId(),
                        inputVerificationCode
                    )
                )
            }
        }

    }

    private fun disableEditTextsKeyListener() {
        binding.apply {
            etFirstDigit.disableKeyListeners(
                etSecondDigit,
                etThirdDigit,
                etFourthDigit,
                etFifthDigit,
                etSixthDigit
            )
        }
    }

    private fun focusOnTheFirstDigit() {
        binding.etFirstDigit.requestFocus()
    }

    private fun returnBackToTheNumberInput() {
        binding.ibBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun enableNumericKeyboardListeners() {
        setupClickingOnOne()
        setupClickingOnTwo()
        setupClickingOnThree()
        setupClickingOnFour()
        setupClickingOnFive()
        setupClickingOnSix()
        setupClickingOnSeven()
        setupClickingOnEight()
        setupClickingOnNine()
        setupClickingOnZero()
    }


    private fun setupClickingOnOne() {
        binding.apply {
            tvOne.setOnNumericClickListener(
                view,
                etFirstDigit,
                etSecondDigit,
                etThirdDigit,
                etFourthDigit,
                etFifthDigit,
                etSixthDigit
            )


        }

    }

    private fun setupClickingOnTwo() {
        binding.apply {

            tvTwo.setOnNumericClickListener(
                view,
                etFirstDigit,
                etSecondDigit,
                etThirdDigit,
                etFourthDigit,
                etFifthDigit,
                etSixthDigit
            )


        }

    }

    private fun setupClickingOnThree() {
        binding.apply {

            tvThree.setOnNumericClickListener(
                view,
                etFirstDigit,
                etSecondDigit,
                etThirdDigit,
                etFourthDigit,
                etFifthDigit,
                etSixthDigit
            )

        }
    }

    private fun setupClickingOnFour() {
        binding.apply {

            tvFour.setOnNumericClickListener(
                view,
                etFirstDigit,
                etSecondDigit,
                etThirdDigit,
                etFourthDigit,
                etFifthDigit,
                etSixthDigit
            )

        }
    }

    private fun setupClickingOnFive() {
        binding.apply {

            tvFive.setOnNumericClickListener(
                view,
                etFirstDigit,
                etSecondDigit,
                etThirdDigit,
                etFourthDigit,
                etFifthDigit,
                etSixthDigit
            )

        }
    }

    private fun setupClickingOnSix() {
        binding.apply {

            tvSix.setOnNumericClickListener(
                view,
                etFirstDigit,
                etSecondDigit,
                etThirdDigit,
                etFourthDigit,
                etFifthDigit,
                etSixthDigit
            )

        }
    }

    private fun setupClickingOnSeven() {
        binding.apply {

            tvSeven.setOnNumericClickListener(
                view,
                etFirstDigit,
                etSecondDigit,
                etThirdDigit,
                etFourthDigit,
                etFifthDigit,
                etSixthDigit
            )

        }
    }

    private fun setupClickingOnEight() {
        binding.apply {

            tvEight.setOnNumericClickListener(
                view,
                etFirstDigit,
                etSecondDigit,
                etThirdDigit,
                etFourthDigit,
                etFifthDigit,
                etSixthDigit
            )
        }
    }

    private fun setupClickingOnNine() {
        binding.apply {

            tvNine.setOnNumericClickListener(
                view,
                etFirstDigit,
                etSecondDigit,
                etThirdDigit,
                etFourthDigit,
                etFifthDigit,
                etSixthDigit
            )

        }
    }

    private fun setupClickingOnZero() {
        binding.apply {

            tvZero.setOnNumericClickListener(
                view,
                etFirstDigit,
                etSecondDigit,
                etThirdDigit,
                etFourthDigit,
                etFifthDigit,
                etSixthDigit
            )

        }
    }


    private fun addBackspaceListener() {
        binding.apply {
            ibBackspace.setOnClickListener {
                view?.deleteACharacterDependingOnFocus(
                    etFirstDigit,
                    etSecondDigit,
                    etThirdDigit,
                    etFourthDigit,
                    etFifthDigit,
                    etSixthDigit,
                )

            }
        }

    }

    private fun moveToTheNextDigit() {
        binding.apply {
            requestFocusOnTheNextDigit(etFirstDigit, etSecondDigit)
            requestFocusOnTheNextDigit(etSecondDigit, etThirdDigit)
            requestFocusOnTheNextDigit(etThirdDigit, etFourthDigit)
            requestFocusOnTheNextDigit(etFourthDigit, etFifthDigit)
            requestFocusOnTheNextDigit(etFifthDigit, etSixthDigit)

        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        viewModel.firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                when (task.isSuccessful) {
                    true -> {
                        viewModel.isUserAuthenticated()
                        findNavController().navigate(R.id.profileFragment)
                        showSnackbar(view, "You have successfully authenticated!")
                        task.result.user
                    }
                    else ->
                        showSnackbar(view, "Authentication process failed. Try again!")
                }
                when (task.exception) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        when (attemptsToVerifyPhoneNumber) {
                            1 -> {
                                showSnackbar(
                                    view,
                                    "try again!"
                                )
                                attemptsToVerifyPhoneNumber++
                            }
                            2 -> {

                                showSnackbar(
                                    view,
                                    "The verification code entered is invalid! You have $attemptsToVerifyPhoneNumber left!"
                                )
                                attemptsToVerifyPhoneNumber++
                            }
                            3 -> {
                                findNavController().navigate(VerifyAuthenticationFragmentDirections.actionVerifyAuthenticationFragmentToVerificationDialogFragment3())
                            }
                        }
                    }

                }
            }
    }


    private fun setupCountDownTimer() {
        countDownTimer = object : CountDownTimer(120000, 1000) {
            override fun onTick(p0: Long) {
                timeInSeconds = p0
                updateCountDownTimer()
            }

            override fun onFinish() {
                countDownTimer.cancel()
                if (view != null)
                    binding.tvCountDownTimer.isVisible = false
                if (view != null)
                    binding.tvGetVerificationCode.isVisible = true
            }
        }
        countDownTimer.start()
    }

    private fun resendVerificationCode(
        phoneNumber: String,
        token: PhoneAuthProvider.ForceResendingToken?
    ) {
        val optionsBuilder = PhoneAuthOptions.newBuilder(viewModel.firebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(viewModel.provideCallback(requireContext()))
        if (token != null) {
            optionsBuilder.setForceResendingToken(token)
        }
        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
    }

    private fun focusOnAnotherEditTextWhileClearingTheEditText() {

    }

    private fun requestFocusOnTheNextDigit(
        editTextUserIsFocusedOn: TextInputEditText,
        editTextToRequestAFocusOn: TextInputEditText
    ) {
        editTextUserIsFocusedOn.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                editTextUserIsFocusedOn.text?.length?.let {
                    editTextToRequestAFocusOn.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

}