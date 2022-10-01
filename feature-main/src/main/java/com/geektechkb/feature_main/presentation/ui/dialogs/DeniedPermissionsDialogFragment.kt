package com.geektechkb.feature_main.presentation.ui.dialogs

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseDialogFragmentWithoutViewModel
import com.geektechkb.feature_main.databinding.FragmentDeniedPermissionsDialogBinding

class DeniedPermissionsDialogFragment :
    BaseDialogFragmentWithoutViewModel<FragmentDeniedPermissionsDialogBinding>(com.geektechkb.feature_main.R.layout.fragment_denied_permissions_dialog) {
    override val binding by viewBinding(FragmentDeniedPermissionsDialogBinding::bind)


    override fun setupListeners() {
        setupClickClose()
        setupClickCancel()

    }

    private fun setupClickCancel() {
        binding.btnSettings.setOnClickListener {
            startActivity(
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(
                    Uri.parse("package:${requireActivity().packageName}")
                )
            )
            dismiss()
        }
    }

    private fun setupClickClose() {
        binding.btnCancel.setOnClickListener { findNavController().navigateUp() }
    }

}