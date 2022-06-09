package com.geektechkb.feature_auth.presentation.ui.fragments.auth.verification

import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseFragment
import com.geektechkb.core.extensions.*
import com.geektechkb.feature_auth.R
import com.geektechkb.feature_auth.databinding.FragmentVerifyAuthenticationBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.PhoneAuthCredential
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class VerifyAuthenticationFragment :
    BaseFragment<FragmentVerifyAuthenticationBinding, VerifyAuthenticationViewModel>(R.layout.fragment_verify_authentication) {
    override val binding by viewBinding(FragmentVerifyAuthenticationBinding::bind)
    override val viewModel: VerifyAuthenticationViewModel by viewModels()
    private val args: VerifyAuthenticationFragmentArgs by navArgs()
    private var timeInSeconds = 0L
    private var attemptsToVerifyPhoneNumberAvailable = 3
    private lateinit var countDownTimer: CountDownTimer

    override fun assembleViews() {
        setPhoneNumberCodeWasSentTo()
        updateCountDownTimer()
        setupCountDownTimer()

    }

    private fun setPhoneNumberCodeWasSentTo() {
        val phoneNumberVerificationCodeWasSentTo =
            String.format(
                getString(R.string.verification_code_was_sent_to_the_entered_phone),
                args.phoneNumber
            )
        binding.tvVerificationCodeWasSent.text = phoneNumberVerificationCodeWasSentTo
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
            resendVerificationCode(args.phoneNumber)
        }
    }

    private fun verifyPhoneNumberUsingCode() {
        binding.apply {
            btnContinue.setOnClickListener {
                etFirstDigit.checkWhetherEditTextsAreNullOrEmpty(
                    etSecondDigit,
                    etThirdDigit,
                    etFourthDigit,
                    etFifthDigit,
                    etSixthDigit
                )

                signInWithPhoneAuthCredential(
                    viewModel.verifyPhoneNumberWithCode(
                        viewModel.getVerificationId(),
                        etFirstDigit.retrieveVerificationCode(
                            etSecondDigit,
                            etThirdDigit,
                            etFourthDigit,
                            etFifthDigit,
                            etSixthDigit
                        )


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
            findNavController().navigateSafely(R.id.action_verifyAuthenticationFragment_to_signUpFragment)
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
                etFirstDigit.deleteACharacterThenFocusOnThePreviousDigit(
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
            establishProperFocusingOnTheNextDigit()


        }
    }

    private fun establishProperFocusingOnTheNextDigit() {
        binding.apply {
            etFirstDigit.requestFocusOnTheNextDigit(etSecondDigit)
            etSecondDigit.requestFocusOnTheNextDigit(etThirdDigit)
            etThirdDigit.requestFocusOnTheNextDigit(etFourthDigit)
            etFourthDigit.requestFocusOnTheNextDigit(etFifthDigit)
            etFifthDigit.requestFocusOnTheNextDigit(etSixthDigit)
        }


    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {

        viewModel.signInWithPhoneAuthCredential(
            viewModel.firebaseAuth,
            credential,
            requireActivity(),
            userSuccessfullyVerifiedTheirPhoneNumber = {
                viewModel.isUserAuthenticated()
                findNavController().directionsSafeNavigation(
                    VerifyAuthenticationFragmentDirections.actionVerifyAuthenticationFragmentToCreateProfileFragment(
                        args.phoneNumber
                    )
                )
                showShortDurationSnackbar("Вы успешно авторизировались!")
            }, authenticationProcessFailed = {

                showShortDurationSnackbar("Процесс аутентификации провалился. Повторите еще раз!")
            }, ifUserHasEnteredInvalidCredentials = {
                when (attemptsToVerifyPhoneNumberAvailable) {
                    0 -> findNavController().navigate(
                        R.id.attemptsToVerifyPhoneNumberExceededDialogFragment
                    )
                    else -> {
                        attemptsToVerifyPhoneNumberAvailable--
                        showLongDurationSnackbar(
                            "Введенный код подтверждения неверный. У вас осталось $attemptsToVerifyPhoneNumberAvailable попытки!"
                        )
                    }
                }
            }

        )
    }

    private fun resendVerificationCode(
        phoneNumber: String,
    ) {
        viewModel.resendVerificationCode(
            viewModel.firebaseAuth,
            phoneNumber,
            requireActivity(),
            viewModel.provideCallbacks(
                authenticationSucceeded =
                {
                    showShortDurationSnackbar("You have successfully authenticated")
                },
                authInvalidCredentialsError = {
                    showShortDurationSnackbar("The phone number you entered was wrong")
                },
                tooManyRequestsError = {
                    showShortDurationSnackbar(

                        "Looks like you have used all of the requests available"
                    )
                }), viewModel.getForceResendingToken()
        )
    }


    private fun TextInputEditText.deleteACharacterThenFocusOnThePreviousDigit(
        vararg digits: TextInputEditText
    ) {
        when (requireView().findFocus()) {
            this -> this.text?.clear()
            digits[0] -> {
                digits[0].text?.clear()
                this.requestFocus()
            }
            digits[1] -> {
                digits[1].text?.clear()
                digits[0].requestFocus()
            }
            digits[2] -> {
                digits[2].text?.clear()
                digits[1].requestFocus()
            }
            digits[3] -> {
                digits[3].text?.clear()
                digits[2].requestFocus()
            }
            digits[4] -> {
                digits[4].text?.clear()
                digits[3].requestFocus()
            }
        }
    }


    private fun TextInputEditText.requestFocusOnTheNextDigit(
        editTextToRequestAFocusOn: TextInputEditText
    ) {
        addTextChangedListenerAnonymously(doSomething = {
            if (text?.length == 1)
                editTextToRequestAFocusOn.requestFocus()
        })
    }

    private fun TextView.setOnNumericClickListener(
        view: View?,
        vararg allDigits: TextInputEditText,

        ) {
        setOnClickListener {
            view?.appendTextDependingOnTheFocus(
                allDigits = allDigits,
                text.toString()

            )
        }
    }


    private fun View.appendTextDependingOnTheFocus(
        vararg allDigits: TextInputEditText,
        textToAppend: CharSequence
    ) {
        when (rootView.findFocus()) {
            allDigits[0] -> allDigits[0].text?.append(textToAppend)
            allDigits[1] -> allDigits[1].text?.append(textToAppend)
            allDigits[2] -> allDigits[2].text?.append(textToAppend)
            allDigits[3] -> allDigits[3].text?.append(textToAppend)
            allDigits[4] -> allDigits[4].text?.append(textToAppend)
            allDigits[5] -> allDigits[5].text?.append(textToAppend)
        }
    }


    private fun TextInputEditText.retrieveVerificationCode(
        vararg digits: TextInputEditText,
    ) =
        text.toString() + digits[0].text + digits[1].text + digits[2].text + digits[3].text + digits[4].text

    private fun TextInputEditText.disableKeyListeners(
        vararg digits: TextInputEditText
    ) {
        keyListener = null
        digits[0].keyListener = null
        digits[1].keyListener = null
        digits[2].keyListener = null
        digits[3].keyListener = null
        digits[4].keyListener = null
    }

    private fun TextInputEditText.checkWhetherEditTextsAreNullOrEmpty(
        vararg digits: TextInputEditText
    ): Boolean {
        return if (this.text.isNullOrEmpty() || digits[0].text.isNullOrEmpty() || digits[1].text.isNullOrEmpty()
            || digits[2].text.isNullOrEmpty() || digits[3].text.isNullOrEmpty() ||
            digits[4].text.isNullOrEmpty() || digits[5].text.isNullOrEmpty()
        ) {
            true
        } else {
            showShortDurationSnackbar("Код верификации состоит из 6 чисел, и все они должны быть заполнены")
            false
        }
    }
}