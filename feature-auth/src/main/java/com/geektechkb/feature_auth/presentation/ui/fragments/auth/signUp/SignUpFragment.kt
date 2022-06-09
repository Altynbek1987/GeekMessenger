package com.geektechkb.feature_auth.presentation.ui.fragments.auth.signUp

import android.widget.TextView
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseFragment
import com.geektechkb.core.extensions.addTextChangedListenerAnonymously
import com.geektechkb.core.extensions.directionsSafeNavigation
import com.geektechkb.feature_auth.R
import com.geektechkb.feature_auth.databinding.FragmentSignUpBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.santalu.maskara.Mask
import com.santalu.maskara.MaskChangedListener
import com.santalu.maskara.MaskStyle
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SignUpFragment :
    BaseFragment<FragmentSignUpBinding, SignUpViewModel>(R.layout.fragment_sign_up) {
    override val binding by viewBinding(FragmentSignUpBinding::bind)
    override val viewModel: SignUpViewModel by hiltNavGraphViewModels(R.id.authorization_graph)
    private var phoneNumberLength: Int? = null
    private lateinit var maskChangedListener: MaskChangedListener


    override fun assembleViews() {
        addMaskForEditText()

    }

    private fun addMaskForEditText() {
        binding.etPhone.addMaskChangeListener()
    }

    override fun setupListeners() {
        addBackspaceListener()

        setupNumericKeyboardListener()
        openPhoneNumberVerificationDialog()
        clearPhoneNumberInputField()
        disableEditTextKeyListener()
    }

    private fun disableEditTextKeyListener() {
        binding.etPhone.keyListener = null
    }

    private fun clearPhoneNumberInputField() {
        binding.ibClearPhoneNumber.setOnClickListener {
            binding.etPhone.text?.clear()
        }
    }

    private fun openPhoneNumberVerificationDialog() {
        binding.btnContinue.setOnClickListener {
            if (phoneNumberLength != 9) {
                binding.tlPhone.setFixedError(getString(R.string.your_phone_number_must_contain_15_digits))
            } else {
                findNavController().directionsSafeNavigation(
                    SignUpFragmentDirections.actionSignUpFragmentToPhoneVerificationDialogFragment(
                        "${binding.tlPhone.prefixText}  ${binding.etPhone.text?.trim()}"
                    )
                )
            }

        }
    }

    private fun addBackspaceListener() {

        binding.ibBackspace.setOnClickListener {

            binding.etPhone.deleteASingleCharacterInEditTextPressingBackspace()
        }

    }


    private fun setupNumericKeyboardListener() {
        binding.apply {
            etPhone.setOnDigitsClickListener(
                tvOne,
                tvTwo,
                tvThree,
                tvFour,
                tvFive,
                tvSix,
                tvSeven,
                tvEight,
                tvNine,
                tvZero,
                textInputLayout = tlPhone
            )
        }
    }

    private fun TextInputEditText.deleteASingleCharacterInEditTextPressingBackspace() {
        val cursorPosition = selectionStart
        if (cursorPosition > 0)
            text?.delete(cursorPosition - 1, cursorPosition)

    }

    private fun TextInputEditText.deleteMultipleCharactersInEditTextByHoldingBackspace() {
        val cursorPosition = selectionStart
        if (cursorPosition > 0) {
            text?.delete(cursorPosition - 1, cursorPosition)
            setSelection(cursorPosition - 1)
        }


    }

    private fun TextInputEditText.setOnDigitsClickListener(
        vararg digits: TextView,
        textInputLayout: TextInputLayout
    ) {

        digits[0].setOnNumericClickListener(this)
        digits[1].setOnNumericClickListener(this)
        digits[2].setOnNumericClickListener(this)
        digits[3].setOnNumericClickListener(this)
        digits[4].setOnNumericClickListener(this)
        digits[5].setOnNumericClickListener(this)
        digits[6].setOnNumericClickListener(this)
        digits[7].setOnNumericClickListener(this)
        digits[8].setOnNumericClickListener(this)
        digits[9].setOnNumericClickListener(this)
        addTextChangedListenerAnonymously(doSomethingAfterTextChanged = {
            textInputLayout.error = null
        })
    }

    private fun TextView.setOnNumericClickListener(editText: TextInputEditText) {
        setOnClickListener {
            editText.append(text)
            phoneNumberLength = maskChangedListener.unMasked.length
        }
    }

    private fun TextInputLayout.setFixedError(errorTxt: CharSequence?) {
        if (error != errorTxt) {
            error = errorTxt
        }
    }

    private fun TextInputEditText.addMaskChangeListener() {
        maskChangedListener = MaskChangedListener(
            Mask(
                value = " ___ ___ ___",
                character = '_',
                style = MaskStyle.PERSISTENT
            )
        )
        addTextChangedListener(maskChangedListener)
    }
}