package com.geektechkb.feature_main.presentation.ui.fragments.chat

import androidx.lifecycle.viewModelScope
import com.geektechkb.core.base.BaseViewModel
import com.geektechkb.feature_main.domain.models.User
import com.geektechkb.feature_main.domain.useCases.FetchPagedMessagesUseCase
import com.geektechkb.feature_main.domain.useCases.FetchUserUseCase
import com.geektechkb.feature_main.domain.useCases.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val fetchPagedMessagesUseCase: FetchPagedMessagesUseCase,
    private val fetchUserUseCase: FetchUserUseCase,
) : BaseViewModel() {

    private val _userState = mutableUiStateFlow<User>()
    val userState = _userState.asStateFlow()

    fun sendMessage(
        id: String,
        receiverPhoneNumber: String,
        message: String,
        image: String? = null,
        mediaType: String? = null,
        videoDuration: String? = null,
        timeMessageWasSent: String,
        messageId: String,
    ) {
        viewModelScope.launch {
            sendMessageUseCase(
                id,
                receiverPhoneNumber,
                message,
                image,
                mediaType,
                videoDuration,
                timeMessageWasSent,
                messageId
            )
        }
    }

    fun fetchPagedMessages(senderPhoneNumber: String, receiverPhoneNumber: String) =
        fetchPagedMessagesUseCase(senderPhoneNumber, receiverPhoneNumber)

    fun fetchUser(phoneNumber: String) =
        fetchUserUseCase(phoneNumber).gatherRequest(_userState)
}