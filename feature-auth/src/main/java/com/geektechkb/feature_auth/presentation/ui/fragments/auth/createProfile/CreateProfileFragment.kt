package com.geektechkb.feature_auth.presentation.ui.fragments.auth.createProfile

import android.Manifest
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseFragment
import com.geektechkb.core.extensions.*
import com.geektechkb.feature_auth.R
import com.geektechkb.feature_auth.databinding.FragmentCreateProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateProfileFragment :
    BaseFragment<FragmentCreateProfileBinding, CreateProfileViewModel>(R.layout.fragment_create_profile) {
    override val binding by viewBinding(FragmentCreateProfileBinding::bind)
    override val galleryViewModel by viewModels<CreateProfileViewModel> ()
    private val args: CreateProfileFragmentArgs by navArgs()
    private var uri: Uri? = null

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
                if (etName.text.isNullOrEmpty() || etName.text.isNullOrBlank()) {
                    etName.error = "Это поле обязательно для заполнения"
                } else {
                    galleryViewModel.isUserAuthenticated()
                    lifecycleScope.launch {
                        galleryViewModel.authenticateUser(
                            "был(а) недавно",
                            args.phoneNumber,
                            binding.etName.text.toString(),
                            binding.etSurname.text.toString(),
                            uri.toString(),
                            generateRandomId()
                        ) {
                            galleryViewModel.isUserAuthenticated()
                            mainNavController(R.id.nav_host_fragment_container_auth).navigateSafely(
                                R.id.action_profileFragment_to_mainFlowFragment
                            )
                        }
                    }
                }
            }

            binding.imProfile.setOnClickListener {
                if (checkForMultiplePermissionsAndRequestThem(
                        requestPermissionLauncher,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    )
                ) {
                    fileChooserContract.launch("image/*")
                }


            }

        }
        binding.ibBack.setOnClickListener {
            findNavController().navigate(R.id.verifyAuthenticationFragment)
        }
    }

    private fun permissionMessage() {
        if (findNavController().currentDestination?.id != R.id.deniedPermissionsDialogFragment) {
            findNavController().navigate(
                CreateProfileFragmentDirections.actionProfileFragmentToDeniedPermissionsDialogFragment(
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
                binding.tvPhotoSelection.text = "Изменить фото профиля"
                binding.tvText.isVisible = false
            }
        }
}