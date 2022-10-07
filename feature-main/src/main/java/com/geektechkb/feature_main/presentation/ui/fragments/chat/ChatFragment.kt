package com.geektechkb.feature_main.presentation.ui.fragments.chat

import android.Manifest
import android.net.Uri
import androidx.core.net.toUri
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.common.constants.Constants.YEAR_MONTH_DAY_HOURS_MINUTES_SECONDS_DATE_FORMAT
import com.geektechkb.core.base.BaseFragment
import com.geektechkb.core.data.local.preferences.UserPreferencesHelper
import com.geektechkb.core.extensions.*
import com.geektechkb.core.ui.customViews.AudioRecordView
import com.geektechkb.core.utils.AppVoiceRecorder
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.databinding.FragmentChatBinding
import com.geektechkb.feature_main.presentation.ui.adapters.GalleryPicturesAdapter
import com.geektechkb.feature_main.presentation.ui.adapters.MessagesAdapter
import com.geektechkb.feature_main.presentation.ui.fragments.gallerydialogbotomsheet.GalleryBottomSheetViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.card.MaterialCardView
import com.vanniktech.emoji.EmojiPopup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class ChatFragment : BaseFragment<FragmentChatBinding, ChatViewModel>(R.layout.fragment_chat),
    AudioRecordView.Callback {

    override val binding by viewBinding(FragmentChatBinding::bind)
    private var bottomSheetBehavior: BottomSheetBehavior<MaterialCardView>? = null
    private val adapter = GalleryPicturesAdapter(this::onSelect)
    private val messagesAdapter = MessagesAdapter()
    override val viewModel by viewModels<ChatViewModel>()
    private val galleryViewModel: GalleryBottomSheetViewModel by viewModels()
    private val args: ChatFragmentArgs by navArgs()
    private var username: String? = null
    private var imageUri = Uri.EMPTY
    private var stateBottomSheet: Boolean = false
    private val appVoiceRecorder = AppVoiceRecorder()
    private val recordAudioPermissionLauncher =
        createRequestPermissionLauncherToRequestSinglePermission(Manifest.permission.RECORD_AUDIO)
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
        binding.recordView.activity = requireActivity()
        binding.recordView.callback = this
        appVoiceRecorder.createFileForRecordedVoiceMessage(requireContext().getExternalFilesDir(null))
    }

    override fun assembleViews() {
        setupAdapter()
        hideClipAndRecordViewWhenUserTyping()
    }

    private fun changeUserStatusToTyping(receiverPhoneNumber: String?) {
        binding.apply {
            binding.etMessage.addTextChangedListenerAnonymously(doSomethingOnTextChanged = {
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

    private fun checkAdapterItemCountAndHideLayout() {
        when (messagesAdapter.itemCount) {
            0 -> binding.iThereAreNoMessagesYet.root.isVisible = true
            else -> binding.iThereAreNoMessagesYet.root.isVisible = false
        }
    }

    private fun setupAdapter() {
        binding.recyclerview.adapter = messagesAdapter
    }

    private fun hideClipAndRecordViewWhenUserTyping() = with(binding) {
        etMessage.addTextChangedListenerAnonymously(doSomethingOnTextChanged = {
            when (etMessage.text?.length) {
                0 -> {
                    recordView.isGone = false
                    imSendMessage.isGone = true
                    imClip.isVisible = true
                }
                else -> {
                    recordView.isGone = true
                    imSendMessage.isGone = false
                    imClip.isVisible = false
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
        onBackPressed()
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
                actionOnDialogStateDragging = {
                    recordView.isVisible = false
                }, actionOnDialogStateExpanded = {
                    recordView.isVisible = false
                }, actionOnDialogStateHidden = {
                    recordView.isVisible = true
                }
            )
        }
    }

    private fun setupBottomSheet() {
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

    private fun onBackPressed() {
        overrideOnBackPressed {
            if (checkWhetherSoftKeyboardIsVisibleOrNot()) {
                hideSoftKeyboard()
            } else {
                findNavController().navigateSafely(R.id.action_chatFragment_to_homeFragment)
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
                formatCurrentUserTime(YEAR_MONTH_DAY_HOURS_MINUTES_SECONDS_DATE_FORMAT),
                generateRandomId()
            )
            etMessage.text?.clear()
        }
    }

    private fun interactWithToolbarMenu() {
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {

                R.id.btn_call -> {
                    true
                }
                R.id.btn_video_call -> {
                    true
                }
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
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            args.phoneNumber?.let { viewModel.fetchUser(it) }
        }
    }

    override fun launchObservers() {
        subscribeToUser()
        subscribeToMessages()
    }

    private fun subscribeToUser() {
        viewModel.userState.spectateUiState(success = {
            changeUserStatusToTyping(it.phoneNumber)
            binding.imProfile.loadImageWithGlide(it.profileImage)
            binding.tvUsername.text = it.name
            binding.tvUserStatus.text = it.lastSeen
            username = it.name
        })
    }

    private fun subscribeToMessages() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                args.phoneNumber?.let { receiverPhoneNumber ->
                    viewModel.fetchPagedMessages(
                        usersPreferencesHelper.currentUserPhoneNumber,
                        receiverPhoneNumber
                    ).collectLatest {
                        messagesAdapter.setPhoneNumber(
                            usersPreferencesHelper.currentUserPhoneNumber, receiverPhoneNumber
                        )
                        messagesAdapter.submitList(it)
                        checkAdapterItemCountAndHideLayout()
                        binding.recyclerview.scrollToPosition(messagesAdapter.itemCount - 1)
                    }
                }
            }
        }
    }

    private fun onSelect(uri: Uri) {
        imageUri = uri
        stateBottomSheet = true
        findNavController().navigate(
            ChatFragmentDirections.actionChatFragmentToPhotoReviewFragment(
                args.phoneNumber.toString(),
                uri.toString()
            )
        )
    }

    override fun onRecordStart() {
        checkForPermissionStatusAndRequestIt(
            recordAudioPermissionLauncher,
            Manifest.permission.RECORD_AUDIO,
            actionWhenPermissionHasBeenGranted = {
                appVoiceRecorder.startRecordingVoiceMessage(requireContext())
            })
    }

    override fun isReady(): Boolean = true

    override fun onRecordEnd() {
        appVoiceRecorder.stopRecordingVoiceMessage()
        viewModel.sendVoiceMessage(
            appVoiceRecorder.retrieveVoiceMessageFile().toUri().toString(),
            generateRandomId()
        )
    }

    override fun onRecordCancel() {
        appVoiceRecorder.deleteRecordedVoiceMessage()
    }
}