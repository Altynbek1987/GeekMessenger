package com.geektechkb.feature_main.presentation.ui.fragments.group.create

import android.Manifest
import android.annotation.SuppressLint
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
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseFragment
import com.geektechkb.core.data.local.preferences.UserPreferencesHelper
import com.geektechkb.core.extensions.*
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.databinding.FragmentCreateGroupBinding
import com.geektechkb.feature_main.presentation.ui.adapters.CreateGroupAdapter
import com.geektechkb.feature_main.presentation.ui.adapters.GalleryPicturesAdapter
import com.geektechkb.feature_main.presentation.ui.fragments.gallerydialogbotomsheet.GalleryBottomSheetViewModel
import com.geektechkb.feature_main.presentation.ui.fragments.profil.profile.ProfileViewModel
import com.geektechkb.feature_main.domain.models.enums.CropPhotoRequest
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.card.MaterialCardView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CreateGroupFragment :
    BaseFragment<FragmentCreateGroupBinding, CreateGroupViewModel>(R.layout.fragment_create_group) {
    override val binding by viewBinding(FragmentCreateGroupBinding::bind)
    override val viewModel by viewModels<CreateGroupViewModel>()
    private val galleryViewModel: GalleryBottomSheetViewModel by viewModels()
    private val profileViewModel by viewModels<ProfileViewModel>()
    private val args by navArgs<CreateGroupFragmentArgs>()
    private val usersGroupAdapter = CreateGroupAdapter()
    private var bottomSheetBehavior: BottomSheetBehavior<MaterialCardView>? = null
    private val adapter = GalleryPicturesAdapter(this::onSelect)
    private val usersPhoneNumbers = mutableListOf<String>()
    @Inject
    lateinit var preferences: UserPreferencesHelper


    override fun initialize() {
        requestReadStoragePermission()
        binding.recyclerviewGroupCreate.adapter = usersGroupAdapter
        Log.e("anime", binding.recyclerviewGroupCreate.toString())
    }

    @SuppressLint("SetTextI18n")
    override fun assembleViews() {
        args.userList.toList().forEach { user ->
            user.phoneNumber?.let { phoneNumber -> usersPhoneNumbers.add(phoneNumber) }
        }
        binding.tvParticipant.text = "${args.userCount}Участников"
        binding.imProfile.setImage(args.userUri)
        lifecycleScope.launch {
            usersGroupAdapter.submitData(PagingData.from(args.userList.toList()))
            Log.e("ello", args.userList.toString())
        }
    }

    private fun requestReadStoragePermission() {
        val readStorage = Manifest.permission.READ_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(
                requireContext(), readStorage
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(readStorage), 3)
        } else initBottomSheetRecycler()
        setupBottomSheet()
    }

    private fun setupBottomSheet() {
        bottomSheetBehavior =
            BottomSheetBehavior.from(binding.galleryBottomSheet.galleryBottomSheetDialog)
    }

    override fun establishRequest() {
        fetchUser()
    }

    private fun fetchUser() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            profileViewModel.fetchUser(preferences.currentUserName)
        }
    }

    override fun setupListeners() {
        binding.imProfile.setOnSingleClickListener {
            binding.coordinatorGallery.isVisible = true

        }
        binding.toolbarBtnGroup.setOnClickListener {
            findNavController().navigate(CreateGroupFragmentDirections.actionCreateGroupFragmentToNavGroups2())
        }
        overrideOnBackPressed {
            findNavController().navigateSafely(R.id.action_createGroupFragment_to_nav_groups2)
        }
        binding.imProfile.setOnClickListener {
            binding.apply {
                openGalleryBottomSheet(
                    galleryBottomSheet.galleryBottomSheetDialog,
                    bottomSheetBehavior,
                    galleryBottomSheet.appbarLayout,
                    coordinatorGallery,
                    actionOnDialogStateDragging = {
                        openUsersGroupOne.isVisible = false
                        imProfile.isVisible = false
                    },
                    actionOnDialogStateExpanded = {
                        openUsersGroupOne.isVisible = false
                        imProfile.isVisible = false

                    },
                    actionOnDialogStateHidden = {
                        imProfile.isVisible = true
                        openUsersGroupOne.isVisible = true

                    })
            }
        }

        binding.openUsersGroupOne.setOnClickListener {
            if (binding.etText.text.isNotEmpty()) {
                viewModel.addUserToGroup(
                    binding.etText.text.toString(),
                    args.userList.toList(),
                    args.userUri,
                    args.userCount,
                    userNumber = preferences.currentUserPhoneNumber
                )
                findNavController().navigate(
                    CreateGroupFragmentDirections.actionCreateGroupFragmentToGroupChatFragment(
                        userCount = args.userCount,
                        usersPhoneNumbers = usersPhoneNumbers.toTypedArray(),
                        groupName = binding.etText.text.toString(),
                        userNumber = preferences.currentUserPhoneNumber,
                    )
                )
            } else {
                Toast.makeText(requireContext(), "заполни название группы", Toast.LENGTH_SHORT)
                    .show()
            }
        }
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

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) initBottomSheetRecycler()
    }

    private fun onSelect(uri: Uri) {
        findNavController().navigate(
            CreateGroupFragmentDirections.actionCreateGroupFragmentToCropPhotoFragment(
                uri.toString(),
                CropPhotoRequest.CREATE_PROFILE,
                userCount = args.userCount,
                userList = args.userList,
                )
        )
    }
}