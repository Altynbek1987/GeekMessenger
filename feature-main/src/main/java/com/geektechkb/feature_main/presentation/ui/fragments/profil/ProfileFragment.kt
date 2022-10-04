package com.geektechkb.feature_main.presentation.ui.fragments.profil

import android.Manifest
import android.net.Uri
import android.util.Log
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseFragment
import com.geektechkb.core.data.local.preferences.UserPreferencesHelper
import com.geektechkb.core.extensions.*
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.databinding.FragmentProfileBinding
import com.geektechkb.feature_main.presentation.ui.adapters.GalleryPicturesAdapter
import com.geektechkb.feature_main.presentation.ui.fragments.gallerydialogbotomsheet.GalleryBottomSheetViewModel
import com.geektechkb.feature_main.presentation.ui.models.enums.CropPhotoRequest
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.card.MaterialCardView
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.avatarview.coil.loadImage
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment :
    BaseFragment<FragmentProfileBinding, ProfileViewModel>(R.layout.fragment_profile) {

    override val binding by viewBinding(FragmentProfileBinding::bind)
    override val viewModel: ProfileViewModel by viewModels()
    private val galleryViewModel: GalleryBottomSheetViewModel by viewModels()
    private val args by navArgs<ProfileFragmentArgs>()
    private var bottomSheetBehavior: BottomSheetBehavior<MaterialCardView>? = null
    private val adapter = GalleryPicturesAdapter(this::onSelect)
    private val readExternalStoragePermissionLauncher =
        createRequestPermissionLauncherToRequestSinglePermission(
            Manifest.permission.READ_EXTERNAL_STORAGE, actionWhenPermissionHasBeenGranted = {
                initBottomSheetRecycler()
                openGalleryBottomSheet()
            },
            actionWhenPermissionHasBeenDenied = {
                if (findNavController().currentDestination?.id != R.id.deniedPermissionsDialogFragment)
                    findNavController().navigateSafely(
                        R.id.deniedPermissionsDialogFragment
                    )
            })

    @Inject
    lateinit var preferences: UserPreferencesHelper

    override fun assembleViews() {
        makeSwitchCheckedIfPhoneNumberIsHidden()
        uploadCroppedImageToFirestoreAndLoadImage()
    }

    private fun uploadCroppedImageToFirestoreAndLoadImage() {
        args.croppedImage?.let {
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.updateUserProfileImage(it).let {
                        viewModel.updateUserProfileImageInFireStore(it)
                    }
                }
            }
        }
    }

    private fun makeSwitchCheckedIfPhoneNumberIsHidden() {
        binding.switchHidePhoneNumber.isChecked = preferences.isPhoneNumberHidden == true
    }

    override fun setupListeners() {
        interactWithToolbarMenu()
        hideBottomSheetOnClick()
        requestPermissionAndOpenBottomSheet()
        navigateToNotificationsAndSoundsFragment()
        navigateToLanguagesFragment()
        hidePhoneNumberOnSwitchChecked()
        backToHomeFragment()
    }

    private fun interactWithToolbarMenu() {
        binding.menuToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.edit_profile -> {
                    findNavController().navigateSafely(R.id.action_profileFragment_to_editProfileFragment)
                    true
                }
                R.id.choose_avatar -> {
                    checkForPermissionStatusAndRequestIt(
                        readExternalStoragePermissionLauncher,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        actionWhenPermissionHasBeenGranted = {
                            binding.apply {
                                initBottomSheetRecycler()
                                openGalleryBottomSheet()
                            }
                        })
                    true
                }
                R.id.delete_avatar -> {
                    binding.avProfileImage.setImageDrawable(null)
                    viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                        viewModel.updateUserProfileImage("")
                    }
                    true
                }
                else -> true
            }
        }
    }

    private fun requestPermissionAndOpenBottomSheet() {
        binding.openBottomSheet.setOnClickListener {
            checkForPermissionStatusAndRequestIt(
                readExternalStoragePermissionLauncher,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                actionWhenPermissionHasBeenGranted = {
                    binding.apply {
                        initBottomSheetRecycler()
                        openGalleryBottomSheet()
                    }
                })
        }
    }

    private fun hideBottomSheetOnClick() {
        binding.galleryBottomSheet.imBack.setOnSingleClickListener {
            binding.coordinatorGallery.isVisible = false
            binding.openBottomSheet.isVisible = true
        }
    }

    private fun navigateToNotificationsAndSoundsFragment() {
        binding.vNotifications.setOnSingleClickListener {
            findNavController().navigateSafely(R.id.action_profileFragment_to_notificationsAndSoundsFragment)
        }
    }

    private fun navigateToLanguagesFragment() {
        binding.vLanguage.setOnSingleClickListener {
            findNavController().navigateSafely(R.id.action_profileFragment_to_languagesFragment)
        }
    }

    private fun hidePhoneNumberOnSwitchChecked() {
        binding.switchHidePhoneNumber.actionOnCheckedChange {
            preferences.isPhoneNumberHidden = it
            viewModel.hideUserPhoneNumber(it)
        }
    }

    private fun backToHomeFragment() {
        binding.toolbarButton.bringToFront()
        binding.toolbarButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun establishRequest() {
        fetchUser()
    }

    private fun fetchUser() {
        viewModel.fetchUser(preferences.currentUserPhoneNumber)
    }

    override fun launchObservers() {
        subscribeToUser()
    }

    private fun subscribeToUser() {
        binding.apply {
            viewModel.userState.spectateUiState(success = { user ->
                user.apply {
                    args.croppedImage?.let {
                        avProfileImage.loadImage(it)
                    } ?: avProfileImage.loadImageAndSetInitialsIfFailed(
                        profileImage,
                        name,
                        cpiProfile
                    )
                    tvName.text = name
                    tvLastSeen.text = lastSeen
                    phoneNumber?.let { phoneNumber ->
                        tvNumber.text =
                            StringBuilder(phoneNumber.substring(0, 4)).append(" ")
                                .append(phoneNumber.substringAfter("+996"))
                    }
                }
            }, error = {
                Log.e("gaypopError", it)
            }, gatherIfSucceed = {
                it.assembleViewVisibility(gOpenBottomSheet, cpiProfile)
            })
        }
    }

    private fun openGalleryBottomSheet() {
        binding.apply {
            openGalleryBottomSheet(
                galleryBottomSheet.galleryBottomSheetDialog,
                bottomSheetBehavior,
                galleryBottomSheet.appbarLayout,
                coordinatorGallery,
                actionOnDialogStateDragging = {
                    openBottomSheet.isVisible = false
                    toolbarButton.isVisible = false
                }, actionOnDialogStateExpanded = {
                    openBottomSheet.isVisible = false
                    toolbarButton.isVisible = false
                }, actionOnDialogStateHidden = {
                    openBottomSheet.isVisible = true
                    toolbarButton.isVisible = true
                }
            )
        }

    }

    private fun initBottomSheetRecycler() {
        bottomSheetBehavior =
            BottomSheetBehavior.from(binding.galleryBottomSheet.galleryBottomSheetDialog)
        binding.galleryBottomSheet.recyclerviewRating.adapter = adapter
        binding.galleryBottomSheet.recyclerviewRating.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                loadPictures()
            }
        })
        loadPictures()
    }

    private fun loadPictures() {
        galleryViewModel.getImagesFromGallery(context = requireContext(), pageSize = 10) {
            if (it.isNotEmpty()) {
                val mutableAdapterList = adapter.currentList.toMutableList()
                mutableAdapterList.addAll(it)
                adapter.submitList(mutableAdapterList)
                adapter.notifyItemRangeInserted(adapter.currentList.size, it.size)
            }
        }
    }

    private fun onSelect(uri: Uri) {
        findNavController().navigate(
            ProfileFragmentDirections.actionProfileFragmentToCropPhotoFragment(
                uri.toString(),
                CropPhotoRequest.PROFILE
            )
        )
    }
}