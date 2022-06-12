package com.geektechkb.feature_main.presentation.ui.fragments.chat

import androidx.lifecycle.viewModelScope
import com.geektechkb.core.base.BaseViewModel
import com.geektechkb.feature_main.domain.useCases.FetchMessagesUseCase
import com.geektechkb.feature_main.domain.useCases.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val fetchMessagesUseCase: FetchMessagesUseCase
) : BaseViewModel() {
    fun sendMessage(id: String, message: String, timeMessageWasSent: Long) {
        viewModelScope.launch {

            sendMessageUseCase(id, message, timeMessageWasSent)
        }
    }
}