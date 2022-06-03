package com.geektechkb.feature_auth.presentation.ui.fragments.auth.signUp

import android.widget.TextView
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseFragment
import com.geektechkb.core.extensions.directionsSafeNavigation
import com.geektechkb.feature_auth.R
import com.geektechkb.feature_auth.databinding.FragmentSignUpBinding
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SignUpFragment :
    BaseFragment<FragmentSignUpBinding, SignUpViewModel>(R.layout.fragment_sign_up) {
    override val binding by viewBinding(FragmentSignUpBinding::bind)
    override val viewModel: SignUpViewModel by hiltNavGraphViewModels(R.id.authorization_graph)
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
            binding.etPhone.clearTextFieldAndAppendTheText()
        }
    }

    private fun openPhoneNumberVerificationDialog() {
        binding.btnContinue.setOnClickListener {
            findNavController().directionsSafeNavigation(
                SignUpFragmentDirections.actionSignUpFragmentToPhoneVerificationDialogFragment(
                    binding.etPhone.text.toString()
                )
            )
        }
    }

    private fun addBackspaceListener() {
        binding.ibBackspace.setOnClickListener {
            binding.etPhone.deleteASingleCharacterInEditTextPressingBackspace()
        }
    }

    private fun setupNumericKeyboardListener() {
        binding.apply {

            tvOne.setOnNumericClickListener(etPhone)
            tvTwo.setOnNumericClickListener(etPhone)
            tvThree.setOnNumericClickListener(etPhone)
            tvFour.setOnNumericClickListener(etPhone)
            tvFive.setOnNumericClickListener(etPhone)
            tvSix.setOnNumericClickListener(etPhone)
            tvSeven.setOnNumericClickListener(etPhone)
            tvEight.setOnNumericClickListener(etPhone)
            tvNine.setOnNumericClickListener(etPhone)
            tvZero.setOnNumericClickListener(etPhone)
        }
    }

    private fun TextInputEditText.deleteASingleCharacterInEditTextPressingBackspace() {
        val cursorPosition = selectionStart
        if (cursorPosition > 0)
            text?.delete(cursorPosition - 1, cursorPosition)

    }

    private fun TextView.setOnNumericClickListener(editText: TextInputEditText) {
        setOnClickListener {
            editText.append(text)
        }
    }


    private fun TextInputEditText.clearTextFieldAndAppendTheText() {

        text?.clear()
        text?.append("+996")

    }
}