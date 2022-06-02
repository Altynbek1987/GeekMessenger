package com.geektechkb.feature_auth.presentation.ui.fragments.auth.signUp

import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseFragment
import com.geektechkb.feature_auth.R
import com.geektechkb.feature_auth.databinding.FragmentSignUpBinding
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
            clearTextField(binding.etPhone)
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
            tvOne.setOnClickListener {
                setOnNumericClickListener(binding.etPhone, tvOne.text)
            }
            tvTwo.setOnClickListener {
                setOnNumericClickListener(binding.etPhone, tvTwo.text)
            }
            tvThree.setOnClickListener {
                setOnNumericClickListener(binding.etPhone, tvThree.text)
            }
            tvFour.setOnClickListener {
                setOnNumericClickListener(binding.etPhone, tvFour.text)
            }
            tvFive.setOnClickListener {
                setOnNumericClickListener(binding.etPhone, tvFive.text)
            }
            tvSix.setOnClickListener {
                setOnNumericClickListener(binding.etPhone, tvSix.text)
            }
            tvSeven.setOnClickListener {
                setOnNumericClickListener(binding.etPhone, tvSeven.text)
            }
            tvEight.setOnClickListener {
                setOnNumericClickListener(binding.etPhone, tvEight.text)
            }
            tvNine.setOnClickListener {
                setOnNumericClickListener(binding.etPhone, tvNine.text)
            }
            tvZero.setOnClickListener {
                setOnNumericClickListener(binding.etPhone, tvZero.text)
            }
//            tvOne.setOnNumericClickListener(etPhone)
//            tvTwo.setOnNumericClickListener(etPhone)
//            tvThree.setOnNumericClickListener(etPhone)
//            tvFour.setOnNumericClickListener(etPhone)
//            tvFive.setOnNumericClickListener(etPhone)
//            tvSix.setOnNumericClickListener(etPhone)
//            tvSeven.setOnNumericClickListener(etPhone)
//            tvEight.setOnNumericClickListener(etPhone)
//            tvNine.setOnNumericClickListener(etPhone)
//            tvZero.setOnNumericClickListener(etPhone)
        }
    }

    private fun deleteASingleCharacterInEditTextPressingBackspace(editText: TextInputEditText) {
        val cursorPosition = editText.selectionStart
        if (cursorPosition > 0)
            editText.text?.delete(cursorPosition - 1, cursorPosition)

    }

    private fun setOnNumericClickListener(editText: TextInputEditText, text: CharSequence) {
        editText.text?.append(text)
    }

    private fun clearTextField(editText: TextInputEditText) {
        editText.text?.clear()
    }


}