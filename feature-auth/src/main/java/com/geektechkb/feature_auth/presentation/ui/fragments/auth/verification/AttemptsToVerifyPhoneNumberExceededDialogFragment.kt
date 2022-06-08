package com.geektechkb.feature_auth.presentation.ui.fragments.auth.verification

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.geektechkb.feature_auth.R
import com.geektechkb.feature_auth.databinding.FragmentVerificationDialogBinding
import com.geektechkb.feature_auth.presentation.ui.fragments.auth.signUp.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AttemptsToVerifyPhoneNumberExceededDialogFragment :
    DialogFragment() {
    private lateinit var binding: FragmentVerificationDialogBinding
    val viewModel: SignUpViewModel by hiltNavGraphViewModels(R.id.authorization_graph)

    @SuppressLint("UseGetLayoutInflater")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentVerificationDialogBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(activity)
            .setView(binding.root)
            .create()
        onClickListener()
        builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return builder
    }

    private fun onClickListener() {
        binding.applyBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}