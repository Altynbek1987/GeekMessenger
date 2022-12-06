package com.geektechkb.feature_main.presentation.ui.dialogs.clearChatHistory

import com.geektechkb.core.base.BaseViewModel
import com.geektechkb.feature_main.domain.useCases.DeleteMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ClearChatHistoryViewModel @Inject constructor(
    private val deleteMessageUseCase: DeleteMessageUseCase
) : BaseViewModel() {
    fun deleteMessage(messageId: String) = deleteMessageUseCase(messageId)
}