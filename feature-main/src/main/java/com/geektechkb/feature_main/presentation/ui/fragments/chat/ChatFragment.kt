package com.geektechkb.feature_main.presentation.ui.fragments.chat

import android.util.Log
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseFragment
import com.geektechkb.core.extensions.*
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.databinding.FragmentChatBinding
import com.geektechkb.feature_main.presentation.ui.adapters.MessagesAdapter
import com.vanniktech.emoji.EmojiPopup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*


@AndroidEntryPoint
class ChatFragment : BaseFragment<FragmentChatBinding, ChatViewModel>(R.layout.fragment_chat) {
    override val binding by viewBinding(FragmentChatBinding::bind)
    private val messagesAdapter = MessagesAdapter()
    override val viewModel: ChatViewModel by viewModels()
    private var isEmojiKeyboardShown = false
    private val args: ChatFragmentArgs by navArgs()


    override fun assembleViews() {
        setupAdapter()
        hideNotificationThatThereAreNoMessages()
        hideCameraIconAndChangeMicrophoneToSendWhenUserInputMessage()
    }

    private fun hideNotificationThatThereAreNoMessages() {
        if (messagesAdapter.itemCount != null) {

            binding.mcvThereAreNoMessages.isVisible = false
        }

    }

    private fun setupAdapter() {
        binding.recyclerview.adapter = messagesAdapter
    }

    private fun hideCameraIconAndChangeMicrophoneToSendWhenUserInputMessage() {
        binding.etMessage.addTextChangedListenerAnonymously(doSomethingOnTextChanged = {
            when (binding.etMessage.text?.length) {
                0 -> {
                    binding.imMicrophone.setImageResource(R.drawable.ic_microphone)
                    binding.imCamera.isVisible = true
                }
                else -> {
                    binding.imMicrophone.setImageResource(R.drawable.ic_send_message)
                    binding.imCamera.isVisible = false
                }
            }
        })


    }

    override fun setupListeners() {
        sendMessage()
        openEmojiSoftKeyboard()
        backToHomeFragment()
        onBackPressed()
        binding.btnMessage.setOnClickListener {
            viewModel.sendMessage(
                "+99655109876",
                "+996704190504",
                binding.etMessage.text.toString(),
                Calendar.getInstance().timeInMillis.toString()
            )
        }
    }

    private fun onBackPressed() {
        overrideOnBackPressed(actionWhenBackButtonPressed = {
            if (checkWhetherSoftKeyboardIsOpenedOrNot())
                hideSoftKeyboard()
            else {
                findNavController().navigate(R.id.homeFragment)
            }

        })
    }

    private fun backToHomeFragment() {
        binding.imBack.setOnClickListener {

            findNavController().navigate(R.id.homeFragment)
        }

    }


    private fun sendMessage() = with(binding) {
        when (imMicrophone.drawable) {
            R.drawable.ic_send_message.toDrawable() -> {
                imMicrophone.setOnClickListener {
                    viewModel.sendMessage(
                        "+996704190504",
                        "+996704190504",
                        etMessage.text.toString(),
                        Calendar.getInstance().timeInMillis.toString()
                    )
                    etMessage.text?.clear()
                }
            }
            else -> null
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
            viewModel.fetchUser("+996552109876")
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
            Log.e("TAG", it.toString())
        })
    }


}