package com.geektechkb.feature_main.presentation.ui.fragments.chat

import android.Manifest
import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent.ACTION_DOWN
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
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
import com.geektechkb.core.ui.customViews.AudioRecordView
import com.geektechkb.core.utils.AppVoiceRecorder
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.data.local.preferences.PreferencesHelper
import com.geektechkb.feature_main.data.remote.services.instence.RetrofitInstance
import com.geektechkb.feature_main.databinding.FragmentChatBinding
import com.geektechkb.feature_main.domain.models.NotificationData
import com.geektechkb.feature_main.domain.models.PushNotification
import com.geektechkb.feature_main.presentation.ui.adapters.GalleryPicturesAdapter
import com.geektechkb.feature_main.presentation.ui.adapters.MessagesAdapter
import com.geektechkb.feature_main.presentation.ui.fragments.gallerydialogbotomsheet.GalleryBottomSheetViewModel
import com.geektechkb.feature_main.domain.models.enums.ChatMessageRequest
import com.geektechkb.feature_main.domain.models.enums.PreviewPhotoRequest
import com.geektechkb.feature_main.domain.models.enums.PreviewVideoRequest
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.card.MaterialCardView
import com.google.gson.Gson
import com.vanniktech.emoji.EmojiPopup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@AndroidEntryPoint
class ChatFragment : BaseFragment<FragmentChatBinding, ChatViewModel>(R.layout.fragment_chat),
    AudioRecordView.Callback {

    override val binding by viewBinding(FragmentChatBinding::bind)
    override val viewModel by viewModels<ChatViewModel>()
    private val galleryViewModel: GalleryBottomSheetViewModel by viewModels()
    private val messagesAdapter = MessagesAdapter(this::openPhotoPreview, this::openVideoPreview)
    private val galleryAdapter = GalleryPicturesAdapter(this::onImageSelected, this::onVideoSelected)
    private val args: ChatFragmentArgs by navArgs()
	private var bottomSheetBehavior: BottomSheetBehavior<MaterialCardView>? = null
	private var stateBottomSheet: Boolean = false
	private var username: String? = null
    private var imageUri: Uri? = null
    private val readExternalStoragePermissionLauncher =
        createRequestPermissionLauncherToRequestSinglePermission(
            Manifest.permission.READ_EXTERNAL_STORAGE, actionWhenPermissionHasBeenGranted = {
                initBottomSheetRecycler()
                setupBottomSheet()
                openBottomSheet()
            },
            actionWhenPermissionHasBeenDenied = {
                if (findNavController().currentDestination?.id != R.id.deniedPermissionsDialogFragment)
                    findNavController().directionsSafeNavigation(
                        ChatFragmentDirections.actionChatFragmentToDeniedPermissionsDialogFragment(
                            getString(com.geektechkb.core.R.string.geekMessenger_application_cant_function_without_needed_permissions_russian)
                        )
                    )
            })
    private val recordAudioPermissionLauncher =
        createRequestPermissionLauncherToRequestSinglePermission(
            Manifest.permission.RECORD_AUDIO,
            actionWhenPermissionHasBeenDenied = {
                if (findNavController().currentDestination?.id != R.id.deniedPermissionsDialogFragment)
                    findNavController().directionsSafeNavigation(
                        ChatFragmentDirections.actionChatFragmentToDeniedPermissionsDialogFragment(
                            getString(com.geektechkb.core.R.string.geekMessenger_application_cant_function_without_needed_permissions_russian)
                        )
                    )
            })
    private val appVoiceRecorder = AppVoiceRecorder()
    @Inject
    lateinit var usersPreferencesHelper: UserPreferencesHelper

    @Inject
    lateinit var preferencesHelper: PreferencesHelper

    override fun initialize() {
        checkForPermissionStatusAndRequestIt(
            recordAudioPermissionLauncher,
            Manifest.permission.RECORD_AUDIO,
            actionWhenPermissionHasBeenGranted = {
            }
        )
        galleryViewModel.shouldVideoBeShown(true)
        appVoiceRecorder.createFileForRecordedVoiceMessage(requireContext().getExternalFilesDir(null))
        sendMediaIfAvailable()
        initializeAudioRecordView()
    }

    private fun initializeAudioRecordView() {
        binding.recordView.apply {
            activity = requireActivity()
            callback = this@ChatFragment
        }
    }

    private fun sendMediaIfAvailable() {
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
        setLightOrDarkWallpapers()
        hideClipAndRecordViewWhenUserTyping()
    }

    private fun setLightOrDarkWallpapers() {
        when (preferencesHelper.isLightMode) {
            true -> binding.root.setBackgroundResource(R.drawable.ic_chat_wallpaper_dark)
            false -> binding.root.setBackgroundResource(R.drawable.ic_chat_wallpaper)
        }
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
                    recordView.visible()
                    imSendMessage.invisible()
                    imClip.visible()
                }
                else -> {
                    recordView.gone()
                    imSendMessage.visible()
                    imClip.gone()
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
        startRecordingAudio()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun startRecordingAudio() {
        binding.recordView.setOnTouchListener { _, event ->
            when (event.action) {
                ACTION_DOWN -> {
                    true
                }
                else ->
                    true
            }
        }
        binding.recordView.setOnClickListener {
            checkForPermissionStatusAndRequestIt(
                recordAudioPermissionLauncher,
                Manifest.permission.RECORD_AUDIO,
                actionWhenPermissionHasBeenGranted = {
                }
            )
        }
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
            hideSoftKeyboard()
            etMessage.clearFocus()
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
                imageUri.toString(),
                timeMessageWasSent = formatCurrentUserTime(
                    YEAR_MONTH_DAY_HOURS_MINUTES_SECONDS_DATE_FORMAT
                ),
                messageId = generateRandomId(),
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
                cpiChatteeProfileImage.bindToUIStateLoading(it)
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
                    ).collectLatest {
                        Log.e("gaypop", it.count().toString())
                        viewModel.addMessagesToDeletion(it)
                        messagesAdapter.setPhoneNumber(
                            usersPreferencesHelper.currentUserPhoneNumber, receiverPhoneNumber
                        )
                        messagesAdapter.submitList(it)
                        binding.recyclerview.smoothScrollToPosition(messagesAdapter.itemCount)
                        binding.iThereAreNoMessagesYet.root.isVisible = it.isEmpty()
                    }
                }
            }
        }
    }

//    private fun sendNotification(pushNotification: PushNotification) = CoroutineScope(
//        Dispatchers.IO
//    ).launch {
//        withContext(Dispatchers.Main) {
//            Toast.makeText(requireContext(), "anime", Toast.LENGTH_SHORT).show()
//        }
//        try {
//            val response = RetrofitInstance.notificationApi.postNotifications(pushNotification)
//            if (response.isSuccessful) {
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(
//                        requireContext(),
//                        "Response${Gson().toJson(response)}",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            } else {
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(
//                        requireContext(),
//                        response.errorBody().toString(),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//        } catch (e: Exception) {
//            withContext(Dispatchers.Main) {
//                Toast.makeText(requireContext(), "anime", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    private fun onImageSelected(uri: Uri) {
        imageUri = uri
        stateBottomSheet = true
        findNavController().directionsSafeNavigation(
            ChatFragmentDirections.actionChatFragmentToPhotoPreviewFragment(
                groupName = null,
                usersPhoneNumber = null,
                phoneNumber = args.phoneNumber.toString(),
                photo = uri.toString(),
                photoPreview = PreviewPhotoRequest.SEND_PHOTO,
                photoCount = 0,
                time = "",
                chatMessage = ChatMessageRequest.CHAT
            )
        )
    }

    private fun onVideoSelected(uri: Uri, videoDuration: String?) {
        findNavController().directionsSafeNavigation(
            ChatFragmentDirections.actionChatFragmentToVideoPreviewFragment(
				chatteePhoneNumber = args.phoneNumber,
				chatteeUsername = username,
				selectedVideo = uri.toString(),
				selectedVideoDuration = videoDuration,
				videoPreview = PreviewVideoRequest.SENT_VIDEO,
				videoCount = 0,
				time = "", chatMessage = ChatMessageRequest.CHAT
			)
        )
    }

    private fun openPhotoPreview(image: String, time: String, photoCount: Int) {
        findNavController().directionsSafeNavigation(
            ChatFragmentDirections.actionChatFragmentToPhotoPreviewFragment(
				 phoneNumber = username.toString(),
				photo = image,
				photoPreview = PreviewPhotoRequest.PHOTO,
				photoCount = photoCount,
				time = time,
				chatMessage = ChatMessageRequest.CHAT
			)
        )
    }

    private fun openVideoPreview(video: String, time: String, videoCount: Int) {
        findNavController().directionsSafeNavigation(
            ChatFragmentDirections.actionChatFragmentToVideoPreviewFragment(
				chatteePhoneNumber = args.phoneNumber,
				chatteeUsername = username.toString(),
				selectedVideo = video,
				selectedVideoDuration = "",
				videoPreview = PreviewVideoRequest.VIDEO,
				videoCount = videoCount,
				time = time, chatMessage = ChatMessageRequest.CHAT
			)
        )
    }

    override fun onRecordStart() = with(binding) {
        val params = recordView.layoutParams as ConstraintLayout.LayoutParams
        params.apply {
            endToEnd = cl.id
            bottomToBottom = cl.id
            width = ConstraintLayout.LayoutParams.MATCH_PARENT
            height = ConstraintLayout.LayoutParams.WRAP_CONTENT
        }
        recordView.layoutParams = params
        appVoiceRecorder.startRecordingVoiceMessage()
    }

    override fun onRecordEnd() = with(binding) {
        val params = recordView.layoutParams as ConstraintLayout.LayoutParams
        params.apply {
            endToEnd = cl.id
            bottomToBottom = cl.id
            width = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                26f,
                resources.displayMetrics
            )
                .toInt()
            height = ConstraintLayout.LayoutParams.WRAP_CONTENT
        }
        recordView.layoutParams = params
        appVoiceRecorder.stopRecordingVoiceMessage()
        viewModel.sendMessage(
            usersPreferencesHelper.currentUserPhoneNumber,
            args.phoneNumber.toString(),
            "",
            appVoiceRecorder.retrieveVoiceMessageFile().toUri().toString(),
            "voiceMessage",
            timeMessageWasSent = formatCurrentUserTime(
                YEAR_MONTH_DAY_HOURS_MINUTES_SECONDS_DATE_FORMAT
            ),
            messageId = generateRandomId(),
        )
    }

    override fun onRecordCancel() = with(binding) {
        val params = recordView.layoutParams as ConstraintLayout.LayoutParams
        params.apply {
            endToEnd = cl.id
            bottomToBottom = cl.id
            width = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                26f,
                resources.displayMetrics
            )
                .toInt()
            height = ConstraintLayout.LayoutParams.WRAP_CONTENT
        }
        recordView.layoutParams = params
        appVoiceRecorder.deleteRecordedVoiceMessage()
        appVoiceRecorder.stopRecordingVoiceMessage()
    }

    override fun isReady() = true


}