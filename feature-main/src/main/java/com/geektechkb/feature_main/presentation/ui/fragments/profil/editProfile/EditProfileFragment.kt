package com.geektechkb.feature_main.presentation.ui.fragments.profil.editProfile

import android.net.Uri
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseFragment
import com.geektechkb.core.data.local.preferences.UserPreferencesHelper
import com.geektechkb.core.extensions.directionsSafeNavigation
import com.geektechkb.core.extensions.loadImageWithGlide
import com.geektechkb.core.extensions.openGalleryBottomSheet
import com.geektechkb.core.extensions.setOnSingleClickListener
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.databinding.FragmentEditProfileBinding
import com.geektechkb.feature_main.presentation.ui.adapters.GalleryPicturesAdapter
import com.geektechkb.feature_main.presentation.ui.fragments.gallerydialogbotomsheet.GalleryBottomSheetViewModel
import com.geektechkb.feature_main.presentation.ui.models.enums.CropPhotoRequest
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.card.MaterialCardView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class EditProfileFragment :
    BaseFragment<FragmentEditProfileBinding, EditProfileViewModel>(R.layout.fragment_edit_profile) {
    override val binding by viewBinding(FragmentEditProfileBinding::bind)
    override val viewModel by viewModels<EditProfileViewModel>()
    private val galleryViewModel: GalleryBottomSheetViewModel by viewModels()
    private val args by navArgs<EditProfileFragmentArgs>()
    private val galleryAdapter = GalleryPicturesAdapter(this::onImageSelected)
    private var bottomSheetBehavior: BottomSheetBehavior<MaterialCardView>? = null

    @Inject
    lateinit var userPreferencesHelper: UserPreferencesHelper

    override fun initialize() {
        instantiateGalleryAdapter()
    }

    private fun instantiateGalleryAdapter() {
        binding.galleryBottomSheet.recyclerviewRating.adapter = galleryAdapter
        binding.galleryBottomSheet.recyclerviewRating.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                getPicturesFromGallery()
            }
        })
        getPicturesFromGallery()
    }

    override fun assembleViews() {
        getCroppedImageAndSetIt()
    }

    override fun setupListeners() {
        openBottomSheetByClickingOnAvatar()
        submitChanges()
        navigateBack()
        hideBottomSheetOnClick()
    }

    private fun openBottomSheetByClickingOnAvatar() {
        binding.mcvProfileAvatar.setOnClickListener {
            binding.apply {
                openGalleryBottomSheet(
                    galleryBottomSheet.galleryBottomSheetDialog,
                    bottomSheetBehavior,
                    galleryBottomSheet.appbarLayout,
                    coordinatorGallery,
                    actionOnDialogStateDragging = {
                        imAvatar.isVisible = false
                        mcvProfileAvatar.isVisible = false
                    },
                    actionOnDialogStateExpanded = {
                        imAvatar.isVisible = false
                        mcvProfileAvatar.isVisible = false
                    }, actionOnDialogStateHidden = {
                        mcvProfileAvatar.isVisible = true
                        imAvatar.isVisible = true
                    }
                )
            }
        }
    }

    private fun submitChanges() = with(binding) {
        ibSubmit.setOnSingleClickListener {
            viewModel.updateUserName(etName.text.toString())
            viewModel.updateUserLastName(etSurname.text.toString())
            getCroppedImageAndSetIt()
        }
    }

    private fun navigateBack() {
        findNavController().navigateUp()
    }

    private fun hideBottomSheetOnClick() {
        binding.galleryBottomSheet.imBack.setOnSingleClickListener {
            binding.coordinatorGallery.isVisible = false
            binding.mcvProfileAvatar.isVisible = true
        }
    }

    override fun establishRequest() {
        fetchUser()
    }

    private fun fetchUser() {
        viewModel.fetchUser(userPreferencesHelper.currentUserPhoneNumber)
    }

    override fun launchObservers() {
        subscribeToUser()
    }

    private fun subscribeToUser() = with(binding) {
        viewModel.userState.spectateUiState(success = { user ->
            user.apply {
                name?.let { name ->
                    etName.setText(name)
                }
                lastName?.let { lastName ->
                    etSurname.setText(lastName)
                }
                profileImage?.let { profileImage ->
                    imAvatar.loadImageWithGlide(profileImage)
                }
            }
        })
    }

    private fun onImageSelected(uri: Uri) {
        findNavController().directionsSafeNavigation(
            EditProfileFragmentDirections.actionEditProfileFragmentToCropPhotoFragment(
                uri.toString(),
                CropPhotoRequest.EDIT_PROFILE
            )
        )
    }

    private fun getPicturesFromGallery() {
        galleryViewModel.getImagesFromGallery(context = requireContext(), pageSize = 10) {
            if (it.isNotEmpty()) {
                val mutableAdapterList = galleryAdapter.currentList.toMutableList()
                mutableAdapterList.addAll(it)
                galleryAdapter.submitList(mutableAdapterList)
                galleryAdapter.notifyItemRangeInserted(galleryAdapter.currentList.size, it.size)
            }
        }
    }

    private fun getCroppedImageAndSetIt() {
        args.croppedProfileAvatar?.let {
            lifecycleScope.launch {
                viewModel.updateUserProfileImage(it).let {
                    binding.imAvatar.loadImageWithGlide(it)
                }
            }
            bottomSheetBehavior =
                BottomSheetBehavior.from(binding.galleryBottomSheet.galleryBottomSheetDialog)
        }
    }
}