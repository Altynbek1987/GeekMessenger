package com.geektechkb.feature_main.presentation.ui.fragments.chat.group

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
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
import com.geektechkb.feature_main.data.local.preferences.PreferencesHelper
import com.geektechkb.feature_main.databinding.FragmentGroupChatBinding
import com.geektechkb.feature_main.presentation.ui.adapters.GalleryPicturesAdapter
import com.geektechkb.feature_main.presentation.ui.adapters.GroupMessagesAdapter
import com.geektechkb.feature_main.presentation.ui.fragments.chat.ChatFragmentDirections
import com.geektechkb.feature_main.presentation.ui.fragments.gallerydialogbotomsheet.GalleryBottomSheetViewModel
import com.geektechkb.feature_main.presentation.ui.models.enums.PreviewPhotoRequest
import com.geektechkb.feature_main.presentation.ui.models.enums.PreviewVideoRequest
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.card.MaterialCardView
import com.vanniktech.emoji.EmojiPopup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class GroupChatFragment :
    BaseFragment<FragmentGroupChatBinding, GroupChatViewModel>(R.layout.fragment_group_chat),
    AudioRecordView.Callback {

    override val binding by viewBinding(FragmentGroupChatBinding::bind)
    private var bottomSheetBehavior: BottomSheetBehavior<MaterialCardView>? = null
    private val galleryAdapter = GalleryPicturesAdapter(this::onImageSelected, this::onSelect)
    private val messagesAdapter = GroupMessagesAdapter()
    override val viewModel by viewModels<GroupChatViewModel>()
    private val galleryViewModel: GalleryBottomSheetViewModel by viewModels()
    private val args: GroupChatFragmentArgs by navArgs()
    private var stateBottomSheet: Boolean = false
    private var imageUri: Uri? = null
    private val appVoiceRecorder = AppVoiceRecorder()
    private val recordAudioPermissionLauncher =
        createRequestPermissionLauncherToRequestSinglePermission(Manifest.permission.RECORD_AUDIO)
    private val readExternalStoragePermissionLauncher =
        createRequestPermissionLauncherToRequestSinglePermission(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            actionWhenPermissionHasBeenDenied = {
                findNavController().navigateSafely(R.id.action_chatFragment_to_deniedPermissionsDialogFragment)
            })
    private var isKeyboardShown: Boolean? = false

    @Inject
    lateinit var preferencesHelper: PreferencesHelper

    @Inject
    lateinit var usersPreferencesHelper: UserPreferencesHelper

    override fun initialize() {
        appVoiceRecorder.createFileForRecordedVoiceMessage(requireContext().getExternalFilesDir(null))
    }

    override fun assembleViews() {
        setupAdapter()
        hideClipAndRecordViewWhenUserTyping()
        setLightOrDarkWallpapers()
    }

    private fun setLightOrDarkWallpapers() {
        when (preferencesHelper.isLightMode) {
            true -> binding.root.setBackgroundResource(R.drawable.ic_chat_wallpaper_dark)
            false -> binding.root.setBackgroundResource(R.drawable.ic_chat_wallpaper)
        }

    }

    private fun checkAdapterItemCountAndHideLayout(
    ) {
        when (messagesAdapter.itemCount) {
            0 -> {
                binding.iThereAreNoMessagesYet.root.isVisible = true
            }
            else -> {
                binding.iThereAreNoMessagesYet.root.isVisible = false
            }
        }
    }

    private fun setupAdapter() {
        binding.recyclerview.adapter = messagesAdapter
    }

    private fun hideClipAndRecordViewWhenUserTyping() = with(binding) {
        etMessage.addTextChangedListenerAnonymously(doSomethingOnTextChanged = {
            when (etMessage.text?.length) {
                0 -> {
                    imMic.isGone = false
                    imSendMessage.isGone = true
                    imClip.isVisible = true
                }
                else -> {
                    imMic.isGone = true
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
            requestReadStoragePermission()
            if (checkForPermissionStatusAndRequestIt(
                    readExternalStoragePermissionLauncher, Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                openBottomSheet()
            }
        }
        binding.imBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun openBottomSheet() {
        binding.coordinatorGallery.isVisible = true
        stateBottomSheet(bottomSheetBehavior, BottomSheetBehavior.STATE_HALF_EXPANDED)
        bottomSheetBehavior?.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (BottomSheetBehavior.STATE_EXPANDED == newState) {
                    showView(binding.galleryBottomSheet.appbarLayout, getActionBarSize())
                } else {
                    hideAppBar(binding.galleryBottomSheet.appbarLayout)
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
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
        galleryViewModel.getImagesFromGallery(context = requireContext(), pageSize = 20) {
            if (it.isNotEmpty()) {
                val mutableAdapterList = galleryAdapter.currentList.toMutableList()
                mutableAdapterList.addAll(it)
                galleryAdapter.submitList(mutableAdapterList)
                galleryAdapter.notifyItemRangeInserted(galleryAdapter.currentList.size, it.size)
            }
            Log.e("GalleryListSize", "${galleryAdapter.currentList.size}")
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) initBottomSheetRecycler()
    }

    private fun showView(view: View, size: Int) {
        val params = view.layoutParams
        params.height = size
        binding.galleryBottomSheet.appbarLayout.isVisible = true
        view.layoutParams = params
    }

    private fun hideAppBar(view: View) {
        val params = view.layoutParams
        params.height = 4
        binding.galleryBottomSheet.appbarLayout.isVisible = false
        view.layoutParams = params
    }

    private fun getActionBarSize(): Int {
        val array =
            requireContext().theme.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize))
        return array.getDimension(0, 0f).toInt()
    }


    private fun onBackPressed() {
        overrideOnBackPressed {
            findNavController().navigateSafely(R.id.action_groupChatFragment_to_groupListFragment)
            isKeyboardShown = null
        }
    }

    private fun sendMessage() = with(binding) {
        imSendMessage.setOnSingleClickListener {
            args.usersPhoneNumbers?.let { it1 ->
                viewModel.sendMessageToGroup(
                    args.groupName,
                    usersPreferencesHelper.currentUserPhoneNumber,
                    it1.toList(),
                    etMessage.text.toString(),
                    formatCurrentUserTime(YEAR_MONTH_DAY_HOURS_MINUTES_SECONDS_DATE_FORMAT),
                    generateRandomId()
                )
            }
            etMessage.text?.clear()
        }
    }

    private fun interactWithToolbarMenu() {
        binding.chatToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.btn_clear_chat -> {
                    /*findNavController().directionsSafeNavigation(
                        ChatFragmentDirections.actionChatFragmentToClearChatHistoryFragment(
                            username.toString()
                        )
                    )*/
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
            findNavController().navigateSafely(R.id.action_groupChatFragment_to_groupListFragment)
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
        viewModel.getGroupInfo(args.groupName)
    }

    override fun launchObservers() {
        subscribeToGroup()
        subscribeToMessages()
    }

    @SuppressLint("SetTextI18n")
    private fun subscribeToGroup() {
        viewModel.groupInfoState.spectateUiState(error = {

        }, success = {
            binding.tvUserStatus.text = it.userCount.toString() + "- Участника"
            binding.tvUsername.text = it.groupName.toString()
            binding.avChatteeProfile.loadImageAndSetInitialsIfFailed(it.groupPhoto, it.groupName)
        })
    }

    private fun subscribeToMessages() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.fetchGroupMessages(
                    args.groupName
                ).collectLatest {
                    it.forEach { groupMessages ->
                        groupMessages?.receiversPhoneNumbers?.let { phoneNumbersList ->
                            phoneNumbersList.forEach { phoneNumber ->
                                messagesAdapter.setPhoneNumber(
                                    usersPreferencesHelper.currentUserPhoneNumber, phoneNumber,
                                )
                            }
                        }
                    }
                    messagesAdapter.submitList(it)
                    checkAdapterItemCountAndHideLayout()
                    binding.recyclerview.scrollToPosition(messagesAdapter.itemCount - 1)
                }
            }
        }
    }

    private fun onImageSelected(uri: Uri) {
        imageUri = uri
        stateBottomSheet = true
        findNavController().directionsSafeNavigation(
            ChatFragmentDirections.actionChatFragmentToPhotoPreviewFragment(
                args.usersPhoneNumbers.toString(),
                uri.toString(),
                PreviewPhotoRequest.SEND_PHOTO,
                0,
                ""
            )
        )
    }




    override fun onRecordStart() {
        if (checkForPermissionStatusAndRequestIt(
                recordAudioPermissionLauncher, Manifest.permission.RECORD_AUDIO
            )
        ) appVoiceRecorder.startRecordingVoiceMessage(requireContext())
    }


    override fun isReady(): Boolean = true


    override fun onRecordEnd() {
        appVoiceRecorder.stopRecordingVoiceMessage()
        viewModel.sendVoiceMessage(
            appVoiceRecorder.retrieveVoiceMessageFile().toUri().toString(), generateRandomId()
        )
    }

    override fun onRecordCancel() {
        appVoiceRecorder.deleteRecordedVoiceMessage()

    }
    private fun onSelect(uri: Uri, videoDuration: String?) {
        findNavController().directionsSafeNavigation(
            ChatFragmentDirections.actionChatFragmentToVideoPreviewFragment(
                chatteePhoneNumber = args.usersPhoneNumbers.toString(),
                chatteeUsername = args.groupName,
                selectedVideo = uri.toString(),
                selectedVideoDuration = videoDuration,
                videoPreview = PreviewVideoRequest.SENT_VIDEO,
                videoCount = 0,
                time = ""
            )
        )
    }


}
