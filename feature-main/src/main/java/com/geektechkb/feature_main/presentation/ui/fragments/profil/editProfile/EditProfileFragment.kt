package com.geektechkb.feature_main.presentation.ui.fragments.profil.editProfile

import android.Manifest
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import com.geektechkb.feature_main.databinding.FragmentEditProfileBinding
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
class EditProfileFragment :
    BaseFragment<FragmentEditProfileBinding, EditProfileViewModel>(R.layout.fragment_edit_profile) {
    override val binding by viewBinding(FragmentEditProfileBinding::bind)
    override val viewModel by viewModels<EditProfileViewModel>()
    private val galleryViewModel: GalleryBottomSheetViewModel by viewModels()
    private val galleryAdapter = GalleryPicturesAdapter(this::onSelect)
    private val args by navArgs<EditProfileFragmentArgs>()
    private var bottomSheetBehavior: BottomSheetBehavior<MaterialCardView>? = null
    private var dialog: Dialog? = null

    private val readExternalStoragePermissionLauncher =
        createRequestPermissionLauncherToRequestSinglePermission(Manifest.permission.READ_EXTERNAL_STORAGE,
            actionWhenPermissionHasBeenGranted = {
                initBottomSheetRecycler()
                openGalleryBottomSheet()
            },
            actionWhenPermissionHasBeenDenied = {
                if (findNavController().currentDestination?.id != R.id.deniedPermissionsDialogFragment) findNavController().navigateSafely(
                    R.id.deniedPermissionsDialogFragment
                )
            })

    @Inject
    lateinit var preferences: UserPreferencesHelper

    override fun assembleViews() {
        uploadCroppedImageToFirestoreAndLoadImage()
    }

    private fun uploadCroppedImageToFirestoreAndLoadImage() {
        args.croppedProfileAvatar?.let {
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    showProgressDialog(R.layout.dialog_progressbar)
                    viewModel.updateUserProfileImage(it).also {
                        viewModel.updateUserProfileImageInFireStore(it)
                        dialog?.dismiss()
                    }
                }
            }
        }
    }

    override fun setupListeners() {
        requestPermissionAndOpenBottomSheet()
        authenticateUser()
        backToHomeFragment()
    }

    private fun requestPermissionAndOpenBottomSheet() {
        binding.avProfile.setOnClickListener {
            checkForPermissionStatusAndRequestIt(readExternalStoragePermissionLauncher,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                actionWhenPermissionHasBeenGranted = {
                    binding.apply {
                        initBottomSheetRecycler()
                        openGalleryBottomSheet()
                    }
                })
        }
        binding.tvPhotoSelection.setOnClickListener {
            checkForPermissionStatusAndRequestIt(readExternalStoragePermissionLauncher,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                actionWhenPermissionHasBeenGranted = {
                    binding.apply {
                        initBottomSheetRecycler()
                        openGalleryBottomSheet()
                    }
                })
        }
    }

    private fun authenticateUser() {
        binding.apply {
            btnSave.setOnClickListener {
                if (etName.text.isNullOrEmpty() || etName.text.isNullOrBlank()) {
                    tilName.isErrorEnabled = true
                    tilName.error = "Это поле обязательно для заполнения"
                } else {
                    viewModel.updateUserName(etName.text.toString())
                    viewModel.updateUserLastName(etSurname.text.toString())
                    findNavController().navigateSafely(R.id.action_editProfileFragment_to_profileFragment)
                }
            }
            ibSubmit.setOnClickListener {
                if (etName.text.isNullOrEmpty() || etName.text.isNullOrBlank()) {
                    tilName.isErrorEnabled = true
                    tilName.error = "Это поле обязательно для заполнения"
                } else {
                    viewModel.updateUserName(etName.text.toString())
                    viewModel.updateUserLastName(etSurname.text.toString())
                    findNavController().navigateSafely(R.id.action_editProfileFragment_to_profileFragment)
                }
            }
            binding.etName.addTextChangedListenerAnonymously(doSomethingOnTextChanged = {
                binding.tilName.error = null
                binding.tilName.isErrorEnabled = false
            })
        }
    }


    private fun backToHomeFragment() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateSafely(R.id.action_editProfileFragment_to_profileFragment)
        }
        overrideOnBackPressed {
            findNavController().navigateSafely(R.id.action_editProfileFragment_to_profileFragment)
        }
        binding.toolbarButton.setOnSingleClickListener {
            findNavController().navigateSafely(R.id.action_editProfileFragment_to_profileFragment)
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

    private fun subscribeToUser() = with(binding) {
        binding.apply {
            viewModel.userState.spectateUiState(success = { user ->
                user.apply {
                    args.croppedProfileAvatar?.let {
                        avProfile.loadImage(it)
                    } ?: avProfile.loadImageAndSetInitialsIfFailed(
                        profileImage, name, cpiCreateProfile, Color.rgb(83, 147, 208)
                    )
                    etName.setText(name)
                    etSurname.setText(lastName)
                }
            }, error = {
                Log.e("gaypopError", it)
            }, gatherIfSucceed = {
                if (args.croppedProfileAvatar == null) cpiCreateProfile.bindToUIStateLoading(it)
            })
        }
    }

    private fun openGalleryBottomSheet() {
        binding.apply {
            openGalleryBottomSheet(galleryBottomSheet.galleryBottomSheetDialog,
                bottomSheetBehavior,
                galleryBottomSheet.appbarLayout,
                coordinatorGallery,
                actionOnDialogStateDragging = {
                    btnSave.isVisible = false
                },
                actionOnDialogStateExpanded = {
                    btnSave.isVisible = false
                },
                actionOnDialogStateHidden = {
                    btnSave.isVisible = true
                })
        }
    }

    private fun initBottomSheetRecycler() {
        bottomSheetBehavior =
            BottomSheetBehavior.from(binding.galleryBottomSheet.galleryBottomSheetDialog)
        binding.galleryBottomSheet.recyclerviewRating.adapter = galleryAdapter
        binding.galleryBottomSheet.recyclerviewRating.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                loadPictures()
            }
        })
        loadPictures()
    private fun onSelect(uri: Uri) {
        findNavController().directionsSafeNavigation(
            EditProfileFragmentDirections.actionEditProfileFragmentToCropPhotoFragment(
				uri.toString(),
				CropPhotoRequest.EDIT_PROFILE,
				emptyArray(),
				0
			)
        )
    }

    private fun loadPictures() {
        galleryViewModel.getImagesFromGallery(context = requireContext(), pageSize = 10) {
            if (it.isNotEmpty()) {
                val mutableAdapterList = galleryAdapter.currentList.toMutableList()
                mutableAdapterList.addAll(it)
                galleryAdapter.submitList(mutableAdapterList)
                galleryAdapter.notifyItemRangeInserted(galleryAdapter.currentList.size, it.size)
            }
        }
    }

    private fun onSelect(uri: Uri) {
        findNavController().directionsSafeNavigation(
            EditProfileFragmentDirections.actionEditProfileFragmentToCropPhotoFragment(
                uri.toString(), CropPhotoRequest.EDIT_PROFILE
            )
        )
    }

    private fun showProgressDialog(
        layout: Int
    ) {
        dialog = Dialog(requireContext())
        with(dialog) {
            this?.setContentView(layout)
            this?.setCanceledOnTouchOutside(false)
            this?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        dialog?.show()
    }


}
