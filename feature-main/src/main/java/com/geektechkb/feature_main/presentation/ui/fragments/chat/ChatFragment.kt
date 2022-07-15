package com.geektechkb.feature_main.presentation.ui.fragments.chat

import androidx.core.net.toUri
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.common.constants.Constants.YEAR_MONTH_DAY_HOURS_MINUTES_SECONDS_DATE_FORMAT
import com.geektechkb.core.base.BaseFragment
import com.geektechkb.core.extensions.*
import com.geektechkb.core.ui.customViews.AudioRecordView
import com.geektechkb.core.ui.customViews.RecordCircleView
import com.geektechkb.core.utils.AppVoiceRecorder
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.data.local.preferences.UserPreferencesHelper
import com.geektechkb.feature_main.databinding.FragmentChatBinding
import com.geektechkb.feature_main.presentation.ui.adapters.MessagesAdapter
import com.vanniktech.emoji.EmojiPopup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class ChatFragment : BaseFragment<FragmentChatBinding, ChatViewModel>(R.layout.fragment_chat),
    AudioRecordView.Callback, RecordCircleView.Callback {
    override val binding by viewBinding(FragmentChatBinding::bind)
    private val messagesAdapter = MessagesAdapter()
    override val viewModel: ChatViewModel by viewModels()
    private val args: ChatFragmentArgs by navArgs()
    private var savedUserStatus: String? = null
    private val appVoiceRecorder = AppVoiceRecorder()


    @Inject
    lateinit var usersPreferencesHelper: UserPreferencesHelper
    override fun initialize() {
        binding.recordView.activity = requireActivity()
        binding.recordView.callback = this
        appVoiceRecorder.createFileForRecordedVoiceMessage(requireContext().getExternalFilesDir(null))

    }


    override fun assembleViews() {
        setupAdapter()
        hideClipAndRecordViewWhenUserTyping()
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {

                R.id.btn_call -> {
                    true
                }
                R.id.btn_video_call -> {
                    true
                }
                R.id.btn_clear_chat -> {
                    showShortDurationSnackbar("fuck")
                    true
                }
                R.id.btn_mute -> {
                    true
                }
                else -> {
                    true
                }
            }
        }
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
        openEmojiSoftKeyboard()
        backToHomeFragment()
        onBackPressed()
    }


    private fun onBackPressed() {
        overrideOnBackPressed(actionWhenBackButtonPressed = {
            if (checkWhetherSoftKeyboardIsOpenedOrNot()) {
                hideSoftKeyboard()
            } else {
                findNavController().navigateUp()
            }

        })
    }

    private fun backToHomeFragment() {
        binding.imBack.setOnSingleClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
    }

    private fun sendMessage() = with(binding) {
        imSendMessage.setOnSingleClickListener {
            viewModel.sendMessage(
                usersPreferencesHelper.currentUserPhoneNumber,
                args.phoneNumber.toString(),
                etMessage.text.toString(),
                formatCurrentUserTime(YEAR_MONTH_DAY_HOURS_MINUTES_SECONDS_DATE_FORMAT),
                generateRandomId()
            )
            etMessage.text?.clear()
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
        lifecycleScope.launch {
            args.phoneNumber?.let { viewModel.fetchUser(it) }
        }
    }


    override fun launchObservers() {
        subscribeToUser()
        subscribeToMessages()
    }

    private fun subscribeToUser() {
        viewModel.userState.spectateUiState(success = {
            savedUserStatus = it.lastSeen
            changeUserStatusToTyping(it.phoneNumber)
            binding.imProfile.loadImageWithGlide(it.profileImage)
            binding.tvUsername.text = it.name
            binding.tvUserStatus.text = it.lastSeen
        })
    }

    private fun subscribeToMessages() {
        viewModel.fetchPagedMessages().spectatePaging(success = {
            messagesAdapter.submitData(it)
            messagesAdapter.notifyDataSetChanged()

            checkAdapterItemCountAndHideLayout()
            binding.recyclerview.scrollToPosition(messagesAdapter.itemCount - 1)
        })
    }


    override fun onRecordStart() {
        appVoiceRecorder.startRecordingVoiceMessage(requireContext())
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

    override fun onSend() {
        appVoiceRecorder.showVoiceMessageFileName()
    }

    override fun onCancel() {
    }
}