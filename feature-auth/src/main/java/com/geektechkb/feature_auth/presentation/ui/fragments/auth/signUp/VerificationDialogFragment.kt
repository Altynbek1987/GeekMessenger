package com.geektechkb.feature_auth.presentation.ui.fragments.auth.signUp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseDialogFragment
import com.geektechkb.feature_auth.R
import com.geektechkb.feature_auth.databinding.FragmentVerificationDialogBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class VerificationDialogFragment :
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
            Toast.makeText(requireContext(), "Повторите попытку еше раз", Toast.LENGTH_SHORT).show()
        }
    }
}