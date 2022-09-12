package com.geektechkb.feature_auth.presentation.ui.fragments.auth.createProfile

import android.Manifest
import android.net.Uri
import android.util.Log
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseFragment
import com.geektechkb.core.extensions.*
import com.geektechkb.feature_auth.R
import com.geektechkb.feature_auth.data.local.preferences.AuthorizePreferences
import com.geektechkb.feature_auth.databinding.FragmentCreateProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreateProfileFragment :
    BaseFragment<FragmentCreateProfileBinding, CreateProfileViewModel>(R.layout.fragment_create_profile) {
    override val binding by viewBinding(FragmentCreateProfileBinding::bind)
    override val viewModel by viewModels<CreateProfileViewModel>()
    private val args: CreateProfileFragmentArgs by navArgs()
    private var uri: Uri? = null
    private val readExternalStoragePermissionLauncher =
        createRequestPermissionLauncherToRequestSinglePermission(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            actionWhenPermissionHasBeenGranted = {
                fileChooserContract.launch("image/*")
            },
            actionWhenPermissionHasBeenDenied = {
                if (findNavController().currentDestination?.id != R.id.deniedPermissionsDialogFragment)
                    findNavController().directionsSafeNavigation(
                        CreateProfileFragmentDirections.actionCreateProfileFragmentToDeniedPermissionsDialogFragment(
                            getString(com.geektechkb.core.R.string.geekMessenger_application_cant_function_without_needed_permissions_russian)
                        )
                    )
            }
        )
    private val fileChooserContract = createFileChooserContractToGetImageUri { imageUri ->
        binding.imProfile.setImage(imageUri.toString())
        uri = imageUri
        binding.tvPhotoSelection.text = "Изменить фото профиля"
        binding.tvText.isVisible = false
    }

    @Inject
    lateinit var authorizePreferences: AuthorizePreferences

    override fun setupListeners() {
        setProfileAvatar()
        authenticateUser()
    }

    private fun setProfileAvatar() {
        binding.imProfile.setOnClickListener {
            checkForPermissionStatusAndRequestIt(
                readExternalStoragePermissionLauncher,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                actionWhenPermissionHasBeenGranted = {
                    fileChooserContract.launch("image/*")
                })
        }
        binding.tvPhotoSelection.setOnClickListener {
            checkForPermissionStatusAndRequestIt(
                readExternalStoragePermissionLauncher,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                actionWhenPermissionHasBeenGranted = {
                    fileChooserContract.launch("image/*")
                })
        }
    }

    private fun authenticateUser() {
        binding.apply {
            btnSignIn.setOnClickListener {
                if (etName.text.isNullOrEmpty() || etName.text.isNullOrBlank()) {
                    tilName.isErrorEnabled = true
                    tilName.error = "Это поле обязательно для заполнения"
                } else {
                    viewModel.authenticateUser(
                        "был(а) недавно",
                        args.phoneNumber,
                        etName.text.toString(),
                        etSurname.text.toString(),
                        uri.toString(),
                        generateRandomId()
                    )
                }
            }
            binding.etName.addTextChangedListenerAnonymously(doSomethingOnTextChanged = {
                binding.tilName.error = null
                binding.tilName.isErrorEnabled = false
            })
        }
    }

    override fun launchObservers() {
        subscribeToAuthentication()
    }

    private fun subscribeToAuthentication() {
        viewModel.authenticationState.spectateUiState(success = {
            authorizePreferences.isAuthorized = true
            mainNavController(R.id.nav_host_fragment_container_auth).navigateSafely(
                R.id.action_profileFragment_to_mainFlowFragment
            )
        }, error = {
            Log.e("gaypopError", it)
        }, gatherIfSucceed = {
            it.assembleViewVisibility(binding.gCreateProfile, binding.cpiCreateProfile)
        })
    }
}