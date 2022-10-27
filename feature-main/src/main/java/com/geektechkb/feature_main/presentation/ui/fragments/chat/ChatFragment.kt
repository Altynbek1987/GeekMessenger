package com.geektechkb.feature_main.presentation.ui.fragments.chat

import android.Manifest
import android.net.Uri
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.common.constants.Constants.YEAR_MONTH_DAY_HOURS_MINUTES_SECONDS_DATE_FORMAT
import com.geektechkb.core.base.BaseFragment
import com.geektechkb.core.data.local.preferences.UserPreferencesHelper
import com.geektechkb.core.extensions.*
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.databinding.FragmentChatBinding
import com.geektechkb.feature_main.presentation.ui.adapters.GalleryPicturesAdapter
import com.geektechkb.feature_main.presentation.ui.adapters.MessagesAdapter
import com.geektechkb.feature_main.presentation.ui.fragments.gallerydialogbotomsheet.GalleryBottomSheetViewModel
import com.geektechkb.feature_main.presentation.ui.models.enums.PreviewPhotoRequest
import com.geektechkb.feature_main.presentation.ui.models.enums.PreviewVideoRequest
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.card.MaterialCardView
import com.vanniktech.emoji.EmojiPopup
import dagger.hilt.android.AndroidEntryPoint
import io.grpc.InternalChannelz.id
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class ChatFragment : BaseFragment<FragmentChatBinding, ChatViewModel>(R.layout.fragment_chat) {

    override val binding by viewBinding(FragmentChatBinding::bind)
    override val viewModel by viewModels<ChatViewModel>()
    private val galleryViewModel: GalleryBottomSheetViewModel by viewModels()
    private val messagesAdapter = MessagesAdapter(this::openPhotoPreview, this::openVideoPreview)
    private val galleryAdapter =
        GalleryPicturesAdapter(this::onImageSelected, this::onVideoSelected)
    private val args: ChatFragmentArgs by navArgs()
    private var bottomSheetBehavior: BottomSheetBehavior<MaterialCardView>? = null
    private var username: String? = null
    private var imageUri: Uri? = null
    private var stateBottomSheet: Boolean = false
    private val readExternalStoragePermissionLauncher =
        createRequestPermissionLauncherToRequestSinglePermission(
            Manifest.permission.READ_EXTERNAL_STORAGE, actionWhenPermissionHasBeenGranted = {
                setupBottomSheet()
            },
            actionWhenPermissionHasBeenDenied = {
                if (findNavController().currentDestination?.id != R.id.deniedPermissionsDialogFragment)
                    findNavController().directionsSafeNavigation(
                        ChatFragmentDirections.actionChatFragmentToDeniedPermissionsDialogFragment(
                            getString(com.geektechkb.core.R.string.geekMessenger_application_cant_function_without_needed_permissions_russian)
                        )
                    )
            })

    @Inject
    lateinit var usersPreferencesHelper: UserPreferencesHelper

    override fun initialize() {
        galleryViewModel.shouldVideoBeShown(true)
        args.image?.let {
            viewModel.sendMessage(
                usersPreferencesHelper.currentUserPhoneNumber,
                args.phoneNumber.toString(),
                "",
                it,
                args.mediaType,
                args.videoDuration,
                formatCurrentUserTime(YEAR_MONTH_DAY_HOURS_MINUTES_SECONDS_DATE_FORMAT),
                generateRandomId()
            )
        }
    }

    override fun assembleViews() {
        setupAdapter()
        hideClipAndRecordViewWhenUserTyping()
    }

    private fun changeUserStatusToTyping(receiverPhoneNumber: String?) {
        binding.apply {
            etMessage.addTextChangedListenerAnonymously(doSomethingOnTextChanged = {
                if (usersPreferencesHelper.currentUserPhoneNumber != receiverPhoneNumber) {
                    tvUserStatus.isVisible = false
                    tvTyping.isVisible = true
                }
            }, doSomethingAfterTextChanged = {
                tvUserStatus.isVisible = true
                tvTyping.isVisible = false
            })
        }
    }

    private fun setupAdapter() {
        binding.recyclerview.adapter = messagesAdapter
    }

    private fun hideClipAndRecordViewWhenUserTyping() = with(binding) {
        etMessage.addTextChangedListenerAnonymously(doSomethingOnTextChanged = {
            when (etMessage.text?.length) {
                0 -> {
                    imMic.visible()
                    imSendMessage.invisible()
                    imClip.visible()
                }
                else -> {
                    imMic.invisible()
                    imSendMessage.visible()
                    imClip.invisible()
                }
            }
        })
    }

    override fun setupListeners() {
        sendMessage()
        expandGalleryDialog()
        openEmojiSoftKeyboard()
        interactWithToolbarMenu()
        backToHomeFragment()
    }

    private fun expandGalleryDialog() {
        if (stateBottomSheet) {
            initBottomSheetRecycler()
            openBottomSheet()
        }
        binding.imClip.setOnSingleClickListener {
            checkForPermissionStatusAndRequestIt(
                readExternalStoragePermissionLauncher,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                actionWhenPermissionHasBeenGranted = {
                    initBottomSheetRecycler()
                    openBottomSheet()
                    setupBottomSheet()
                })
        }
    }

    private fun openBottomSheet() {
        binding.apply {
            openGalleryBottomSheet(
                galleryBottomSheet.galleryBottomSheetDialog,
                bottomSheetBehavior,
                galleryBottomSheet.appbarLayout,
                coordinatorGallery,
            )
        }
    }

    private fun setupBottomSheet() {
        bottomSheetBehavior =
            BottomSheetBehavior.from(binding.galleryBottomSheet.galleryBottomSheetDialog)
    }

    private fun initBottomSheetRecycler() {
        binding.galleryBottomSheet.recyclerviewRating.adapter = galleryAdapter
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
                val mutableAdapterList = galleryAdapter.currentList.toMutableList()
                mutableAdapterList.addAll(it)
                galleryAdapter.submitList(mutableAdapterList)
                galleryAdapter.notifyItemRangeInserted(galleryAdapter.currentList.size, it.size)
            }
        }
    }

    private fun sendMessage() = with(binding) {
        imSendMessage.setOnSingleClickListener {
            viewModel.sendMessage(
                usersPreferencesHelper.currentUserPhoneNumber,
                args.phoneNumber.toString(),
                etMessage.text.toString(),
                "",
                timeMessageWasSent = formatCurrentUserTime(
                    YEAR_MONTH_DAY_HOURS_MINUTES_SECONDS_DATE_FORMAT
                ),
                messageId = generateRandomId(),
                onSuccess = {
                    recyclerview.scrollToPosition(messagesAdapter.itemCount - 1)
                }
            )
            etMessage.text?.clear()
        }
    }

    private fun interactWithToolbarMenu() {
        binding.chatToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.btn_clear_chat -> {
                    findNavController().directionsSafeNavigation(
                        ChatFragmentDirections.actionChatFragmentToClearChatHistoryFragment(
                            username.toString()
                        )
                    )
                    true
                }
                R.id.btn_mute -> {
                    true
                }
                else -> true
            }
        }
    }

    private fun backToHomeFragment() {
        binding.imBack.setOnSingleClickListener {
            findNavController().navigate(R.id.action_chatFragment_to_homeFragment)
        }
    }

    private fun openEmojiSoftKeyboard() {

        binding.apply {

            val emojiPopUp = EmojiPopup(
                root,
                binding.etMessage,

                onEmojiPopupShownListener = { binding.imEmoji.setImageResource(R.drawable.ic_keyboard) },
                onEmojiPopupDismissListener = { binding.imEmoji.setImageResource(R.drawable.ic_emoji) },
            )
            binding.imEmoji.setOnSingleClickListener {
                emojiPopUp.toggle()
            }
        }

    }

    override fun establishRequest() {
        fetchUser()
    }

    private fun fetchUser() {
        args.phoneNumber?.let { viewModel.fetchUser(it) }
    }

    override fun launchObservers() {
        subscribeToUser()
        subscribeToMessages()
    }

    private fun subscribeToUser() {
        binding.apply {
            viewModel.userState.spectateUiState(success = { user ->
                user.apply {
                    changeUserStatusToTyping(phoneNumber)
                    avChatteeProfile.loadImageAndSetInitialsIfFailed(profileImage, name)
                    tvUsername.text = name
                    tvUserStatus.text = lastSeen
                    username = name
                }
            }, gatherIfSucceed = {
                cpiChatteProfileImage.bindToUIStateLoading(it)
            })
        }
    }

    private fun subscribeToMessages() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                args.phoneNumber?.let { receiverPhoneNumber ->
                    viewModel.fetchPagedMessages(
                        usersPreferencesHelper.currentUserPhoneNumber,
                        receiverPhoneNumber
                    ).collect {
                        messagesAdapter.setPhoneNumber(
                            usersPreferencesHelper.currentUserPhoneNumber, receiverPhoneNumber
                        )
                        messagesAdapter.submitList(it)
                        when (messagesAdapter.itemCount == 0 || it.isEmpty()) {
                            true -> binding.iThereAreNoMessagesYet.root.isVisible = true
                            false -> binding.iThereAreNoMessagesYet.root.isVisible = false
                        }
                        binding.recyclerview.scrollToPosition(messagesAdapter.itemCount - 1)
                    }
                }
            }
        }
    }

    private fun onImageSelected(uri: Uri) {
        imageUri = uri
        stateBottomSheet = true
        hideSoftKeyboard()
        findNavController().directionsSafeNavigation(
            ChatFragmentDirections.actionChatFragmentToPhotoPreviewFragment(
                args.phoneNumber.toString(),
                uri.toString(),
                PreviewPhotoRequest.SEND_PHOTO,
                0,
                ""
            )
        )
    }

    private fun onVideoSelected(uri: Uri, videoDuration: String?) {
        hideSoftKeyboard()
        findNavController().directionsSafeNavigation(
            ChatFragmentDirections.actionChatFragmentToVideoPreviewFragment(
                chatteePhoneNumber = args.phoneNumber,
                chatteeUsername = username,
                selectedVideo = uri.toString(),
                selectedVideoDuration = videoDuration,
                videoPreview = PreviewVideoRequest.SENT_VIDEO,
                videoCount = 0,
                time = ""
            )
        )
    }

    private fun openPhotoPreview(image: String, time: String, photoCount: Int) {
        findNavController().directionsSafeNavigation(
            ChatFragmentDirections.actionChatFragmentToPhotoPreviewFragment(
                username.toString(),
                image,
                PreviewPhotoRequest.PHOTO,
                photoCount,
                time
            )
        )
    }

    private fun openVideoPreview(video: String, time: String, videoCount: Int) {
        hideSoftKeyboard()
        findNavController().directionsSafeNavigation(
            ChatFragmentDirections.actionChatFragmentToVideoPreviewFragment(
                chatteePhoneNumber = args.phoneNumber,
                chatteeUsername = username.toString(),
                selectedVideo = video,
                selectedVideoDuration = "",
                videoPreview = PreviewVideoRequest.VIDEO,
                videoCount = videoCount,
                time = time
            )
        )
/*
        binding.cpiChatteProfileImage.bindToUIStateLoading(photoUiState)
*/
    }
}