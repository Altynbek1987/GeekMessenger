package com.geektechkb.feature_main.presentation.ui.dialogs

import android.text.SpannableStringBuilder
import androidx.core.text.bold
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseDialogFragment
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.databinding.FragmentClearChatHistoryBinding
import com.geektechkb.feature_main.presentation.ui.fragments.chat.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ClearChatHistoryFragment :
    BaseDialogFragment<FragmentClearChatHistoryBinding, ChatViewModel>(R.layout.fragment_clear_chat_history) {
    override val binding by viewBinding(FragmentClearChatHistoryBinding::bind)
    override val viewModel by activityViewModels<ChatViewModel>()
    private val args: ClearChatHistoryFragmentArgs by navArgs()

    override fun initialize() {
        setDialogCancelable()
    }

    private fun setDialogCancelable() {
        dialog?.setCancelable(true)
        dialog?.setCanceledOnTouchOutside(true)
    }

    override fun assembleViews() {
        setUsername()
    }

    private fun setUsername() {
        binding.tvDeleteChatHistoryWithFollowingUser.text =
            SpannableStringBuilder("Вы точно хотите очистить историю переписки с ")
                .bold {
                    append(
                        args.usernameToDeleteChatHistoryWith
                    )
                }
    }

    override fun setupListeners() {
        dismissDialog()
        clearChatHistory()
    }

    private fun dismissDialog() {
        binding.tvCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun clearChatHistory() {
        binding.tvDelete.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.messagesState.collectLatest {
                        it.forEach { message ->
                            viewModel.deleteMessage(message?.messageId.toString())
                        }
                    }
                }
            }
            dismiss()
        }
    }
}