package com.geektechkb.feature_auth.presentation.ui.fragments.auth.profile

import android.Manifest
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseFragment
import com.geektechkb.feature_auth.R
import com.geektechkb.feature_auth.databinding.FragmentProfileBinding
import com.geektechkb.feature_auth.presentation.ui.extensions.mainNavController
import com.geektechkb.feature_auth.presentation.ui.extensions.setImage
import com.geektechkb.core.extensions.hasPermissionCheckAndRequest
import com.geektechkb.core.extensions.navigateSafely
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment :
    BaseFragment<FragmentProfileBinding, ProfileViewModel>(R.layout.fragment_profile) {
    override val binding by viewBinding(FragmentProfileBinding::bind)
    override val viewModel: ProfileViewModel by viewModels()
    private lateinit var uri: Uri


    override fun setupListeners() {


        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { isGranted ->
            for (permission in isGranted) {
                when {
                    permission.value -> fileChooserContract.launch("image/*")
                    !shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                        permissionMessage()

                    }
                }
            }
        }
        binding.apply {
            binding.btnSignIn.setOnClickListener {
                if (etName.text.isNullOrEmpty() || etSurname.text.isNullOrEmpty()) {
                    etName.error = "This field mustn't be empty"
                    etSurname.error = "This field mustn't be empty"


                } else {
                    mainNavController().navigateSafely(R.id.action_profileFragment_to_mainFlowFragment)
                }
            }
        }



        binding.imProfile.setOnClickListener {
            if (hasPermissionCheckAndRequest(
                    requestPermissionLauncher,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
            ) {
                fileChooserContract.launch("image/*")
            }
        }

    }

    private fun permissionMessage() {
        if (findNavController().currentDestination?.id != R.id.deniedPermissionsDialogFragment) {
            findNavController().navigate(
                ProfileFragmentDirections.actionProfileFragmentToDeniedPermissionsDialogFragment(
                    "Приложение GeekMessenger не может функционировать без разрешение на галерею устройства. Вы можете включить их в разределе Настройки"
                )
            )
        }
    }


    private val fileChooserContract =
        registerForActivityResult(ActivityResultContracts.GetContent()) { imageUri ->
            if (imageUri != null) {
                binding.imProfile.setImage(imageUri.toString())
                uri = imageUri
            }
        }
}


