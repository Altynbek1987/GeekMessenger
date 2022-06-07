package com.geektechkb.feature_auth.presentation.ui.fragments.auth.createProfile

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseDialogFragment
import com.geektechkb.feature_auth.R
import com.geektechkb.feature_auth.databinding.FragmentDeniedPermissionsDialogBinding

class DeniedPermissionsDialogFragment :
    BaseDialogFragment<FragmentDeniedPermissionsDialogBinding, CreateProfileViewModel>(R.layout.fragment_denied_permissions_dialog) {
    override val binding by viewBinding(FragmentDeniedPermissionsDialogBinding::bind)
    override val viewModel: CreateProfileViewModel by hiltNavGraphViewModels(R.id.authorization_graph)
    private val args: DeniedPermissionsDialogFragmentArgs by navArgs()
    override fun assembleViews() {
        binding.txtDialogPermissionTitle.text = args.text
    }

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