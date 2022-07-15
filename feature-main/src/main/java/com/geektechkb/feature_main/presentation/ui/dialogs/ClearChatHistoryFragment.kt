package com.geektechkb.feature_main.presentation.ui.dialogs

import android.text.SpannableStringBuilder
import androidx.core.text.bold
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseDialogFragmentWithoutViewModel
import com.geektechkb.core.extensions.showShortDurationSnackbar
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.databinding.FragmentClearChatHistoryBinding


class ClearChatHistoryFragment :
    BaseDialogFragmentWithoutViewModel<FragmentClearChatHistoryBinding>(R.layout.fragment_clear_chat_history) {
    override val binding by viewBinding(FragmentClearChatHistoryBinding::bind)
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
            SpannableStringBuilder().append("Вы точно хотите очистить историю переписки с ")
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
            showShortDurationSnackbar("История переписки удалена")
            dismiss()
        }
    }


}