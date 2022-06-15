package com.geektechkb.feature_main.presentation.ui.fragments.chat

import android.util.Log
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseFragment
import com.geektechkb.core.extensions.addTextChangedListenerAnonymously
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.databinding.FragmentChatBinding
import com.geektechkb.feature_main.presentation.ui.adapters.MessagesAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class ChatFragment : BaseFragment<FragmentChatBinding, ChatViewModel>(R.layout.fragment_chat) {
    override val binding by viewBinding(FragmentChatBinding::bind)
    private val messagesAdapter = MessagesAdapter()
    override val viewModel: ChatViewModel by viewModels()


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
        binding.btnMessage.setOnClickListener {
            viewModel.sendMessage(
                "+99655109876",
                "+996704190504",
                binding.etMessage.text.toString(),
                Calendar.getInstance().timeInMillis.toString()
            )
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
        }
    }

    override fun launchObservers() {
        viewModel.fetchPagedMessages().spectatePaging(success = {
            messagesAdapter.submitData(it)
            Log.e("TAG", it.toString())
        })
    }

    override fun establishRequest() {
        viewModel.fetchPagedMessages().spectatePaging(success = {
            messagesAdapter.submitData(it)
            Log.e("TAG", it.toString())
        })
    }


}