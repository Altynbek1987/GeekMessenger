package com.geektechkb.feature_main.presentation.ui.fragments.chat

import android.util.Log
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
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.databinding.FragmentChatBinding
import com.geektechkb.feature_main.presentation.ui.adapters.MessagesAdapter
import com.vanniktech.emoji.EmojiPopup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ChatFragment : BaseFragment<FragmentChatBinding, ChatViewModel>(R.layout.fragment_chat) {
    override val binding by viewBinding(FragmentChatBinding::bind)
    private val messagesAdapter = MessagesAdapter()
    override val viewModel: ChatViewModel by viewModels()
    private val args: ChatFragmentArgs by navArgs()


    override fun assembleViews() {
        setupAdapter()
        hideNotificationThatThereAreNoMessages()
        hideCameraIconAndChangeMicrophoneToSendWhenUserInputMessage()
    }

    private fun hideNotificationThatThereAreNoMessages() {
        if (messagesAdapter.itemCount == 0) {

            binding.mcvThereAreNoMessages.isVisible = false
        }

    }

    private fun setupAdapter() {
        binding.recyclerview.adapter = messagesAdapter
    }

    private fun hideCameraIconAndChangeMicrophoneToSendWhenUserInputMessage() = with(binding) {
        etMessage.addTextChangedListenerAnonymously(doSomethingOnTextChanged = {
            when (etMessage.text?.length) {
                0 -> {
                    imMicrophone.isGone = false
                    imSendMessage.isGone = true
                    imCamera.isVisible = true
                }
                else -> {
                    imMicrophone.isGone = true
                    imSendMessage.isGone = false
                    imCamera.isVisible = false
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
                findNavController().navigate(R.id.homeFragment)
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
                "+996704190504",
                args.phoneNumber.toString(),
                etMessage.text.toString(),
                formatCurrentUserTime(YEAR_MONTH_DAY_HOURS_MINUTES_SECONDS_DATE_FORMAT)
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
            Log.e("gayPop", args.phoneNumber.toString())
        }
    }


    override fun launchObservers() {
        subscribeToMessages()
        subscribeToUser()


    }

    private fun subscribeToUser() {
        viewModel.userState.spectateUiState(success = {
            binding.imProfile.loadImageWithGlide(it.profileImage)
            binding.tvUsername.text = it.name
        })
    }

    private fun subscribeToMessages() {
        viewModel.fetchPagedMessages().spectatePaging(success = {
            messagesAdapter.submitData(it)
        })
    }
}