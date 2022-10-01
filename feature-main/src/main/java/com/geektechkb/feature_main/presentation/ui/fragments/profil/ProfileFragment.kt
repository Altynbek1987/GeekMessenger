package com.geektechkb.feature_main.presentation.ui.fragments.profil

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment :
    BaseFragment<FragmentProfileBinding, ProfileViewModel>(R.layout.fragment_profile) {

    override val binding by viewBinding(FragmentProfileBinding::bind)
    override val viewModel: ProfileViewModel by viewModels()
    private val galleryViewModel: GalleryBottomSheetViewModel by viewModels()
    private val args by navArgs<ProfileFragmentArgs>()
    private var name: String? = null
    private var lastName: String? = null
    private var profileAvatar: String? = null
    private var savedUserStatus: String? = null
    private var bottomSheetBehavior: BottomSheetBehavior<MaterialCardView>? = null
    private val adapter = GalleryPicturesAdapter(this::onSelect)

    @Inject
    lateinit var preferences: UserPreferencesHelper

    override fun assembleViews() {
        makeSwitchCheckedIfPhoneNumberIsHidden()
    }

    private fun makeSwitchCheckedIfPhoneNumberIsHidden() {
        binding.switchHidePhoneNumber.isChecked = preferences.isPhoneNumberHidden == true
    }

    override fun initialize() {
        requestReadStoragePermission()
    }

    private fun requestReadStoragePermission() {
        val readStorage = Manifest.permission.READ_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                readStorage
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(readStorage), 3)
        } else initBottomSheetRecycler()
        setupBottomSheet()
    }

    override fun setupListeners() {
        interactWithToolbarMenu()
        hideBottomSheetOnClick()
        navigateToNotificationsAndSoundsFragment()
        navigateToLanguagesFragment()
        hidePhoneNumberOnSwitchChecked()
        backToHomeFragment()
        binding.openBottomSheet.setOnClickListener {
            binding.apply {
                openGalleryBottomSheet(
                    galleryBottomSheet.galleryBottomSheetDialog,
                    bottomSheetBehavior,
                    galleryBottomSheet.appbarLayout,
                    coordinatorGallery,
                    actionOnDialogStateDragging = {
                        openBottomSheet.isVisible = false
                    }, actionOnDialogStateExpanded = {
                        openBottomSheet.isVisible = false
                    }, actionOnDialogStateHidden = {
                        openBottomSheet.isVisible = true
                    }
                )
            }
        }
        binding.toolbarButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun backToHomeFragment() {
        binding.toolbarButton.setOnClickListener {
            findNavController().navigateUp()
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

    private fun interactWithToolbarMenu() = with(binding) {
        binding.menuToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.edit_profile -> {
                    findNavController().navigateSafely(R.id.action_profileFragment_to_editProfileFragment)
                    true
                }
                R.id.choose_avatar -> {
                    openGalleryBottomSheet(
                        galleryBottomSheet.galleryBottomSheetDialog,
                        bottomSheetBehavior,
                        galleryBottomSheet.appbarLayout,
                        coordinatorGallery,
                        actionOnDialogStateDragging = {
                            openBottomSheet.isVisible = false
                        }, actionOnDialogStateExpanded = {
                            openBottomSheet.isVisible = false
                        }, actionOnDialogStateHidden = {
                            openBottomSheet.isVisible = true
                        }
                    )
                    true
                }
                R.id.delete_avatar -> {
                    binding.imImageProfile.setImageDrawable(null)
                    binding.imImageProfile.drawable.toString()
                    viewLifecycleOwner.lifecycleScope.launchWhenStarted {
//                        viewModel.updateUserProfileImage()
                    }
                    true
                }
                else -> true
            }
        }
    }

    override fun establishRequest() {
        fetchUser()
    }

    private fun fetchUser() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.fetchUser(preferences.currentUserPhoneNumber)
        }
    }

    override fun launchObservers() {
        subscribeToUser()
    }

    private fun subscribeToUser() {
        viewModel.userState.spectateUiState(success = {
            savedUserStatus = it.lastSeen
            profileAvatar = it.profileImage
            if (args.croppedImage == null) {
                binding.imImageProfile.loadImageWithGlide(it.profileImage)
            }
            binding.tvName.text = it.name
            binding.tvLastSeen.text = it.lastSeen
            it.phoneNumber?.let { phoneNumber ->
                binding.tvNumber.text =
                    StringBuilder(phoneNumber.substring(0, 4)).append(" ")
                        .append(phoneNumber.substringAfter("+996"))
            }
            name = it.name
            lastName = it.lastName

        }, error = {
            Log.e("gaypopError", it)
        }, gatherIfSucceed = {
            it.assembleViewVisibility(binding.gProfile, binding.cpiProfile)
        })
    }

    private fun setupBottomSheet() {
        args.croppedImage?.let {
            lifecycleScope.launch {
                viewModel.updateUserProfileImage(it).let {
                    viewModel.updateUserProfileImageInFireStore(it)
                    binding.imImageProfile.loadImageWithGlide(it)
                    Log.e("TAG", it)
                }
            }
        }
        bottomSheetBehavior =
            BottomSheetBehavior.from(binding.galleryBottomSheet.galleryBottomSheetDialog)
    }

    private fun initBottomSheetRecycler() {
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

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            initBottomSheetRecycler()
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