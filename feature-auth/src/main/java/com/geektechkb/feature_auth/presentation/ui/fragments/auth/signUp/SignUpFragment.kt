package com.geektechkb.feature_auth.presentation.ui.fragments.auth.signUp

import android.widget.TextView
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseFragment
import com.geektechkb.feature_auth.R
import com.geektechkb.feature_auth.databinding.FragmentSignUpBinding
import com.github.pinball83.maskededittext.MaskedEditText
import com.google.android.material.textfield.TextInputEditText
import com.timplifier.firebaseauthenticationtest.presentation.extensions.directionsSafeNavigation
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
            binding.etPhone.text?.clear()
            binding.etPhone.text?.append("+996")
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
            deleteASingleCharacterInEditTextPressingBackspace(binding.etPhone)
//            binding.etPhone.deleteASingleCharacterInEditText()
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

    private fun deleteASingleCharacterInEditTextPressingBackspace(editText: TextInputEditText) {
        val cursorPosition = editText.selectionStart
        if (cursorPosition > 0)
            editText.text?.delete(cursorPosition - 1, cursorPosition)

    }

    private fun deleteASingleCharacterInEditTextPressingBackspace(editText: MaskedEditText) {
        val cursorPosition = editText.selectionStart
        if (cursorPosition > 0)
            editText.text?.delete(cursorPosition - 1, cursorPosition)


    }
}

fun TextView.setOnNumericClickListener(editText: MaskedEditText) {
    editText.text?.append(text)
}