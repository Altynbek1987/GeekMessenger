package com.geektechkb.feature_main.presentation.ui.fragments.chat

import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseFragment
import com.geektechkb.core.extensions.addTextChangedListenerAnonymously
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.databinding.FragmentChatBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class ChatFragment : BaseFragment<FragmentChatBinding, ChatViewModel>(R.layout.fragment_chat) {
    override val binding by viewBinding(FragmentChatBinding::bind)
    override val viewModel: ChatViewModel by viewModels()

    override fun assembleViews() {
        hideCameraIconAndChangeMicrophoneToSendWhenUserInputMessage()
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


    }

    private fun sendMessage() {
        when (binding.imMicrophone.drawable) {
            R.drawable.ic_send_message.toDrawable() -> {
                binding.imMicrophone.setOnClickListener {


                    viewModel.sendMessage(
                        binding.etMessage.text.toString(),
                        binding.etMessage.text.toString(),
                        Calendar.getInstance().timeInMillis
                    )
                }


            }
        }
    }


}