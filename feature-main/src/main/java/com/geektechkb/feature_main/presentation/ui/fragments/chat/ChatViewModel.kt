package com.geektechkb.feature_main.presentation.ui.fragments.chat

import androidx.lifecycle.viewModelScope
import com.geektechkb.core.base.BaseViewModel
import com.geektechkb.feature_main.domain.models.User
import com.geektechkb.feature_main.domain.useCases.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val sendVoiceMessageUseCase: SendVoiceMessageUseCase,
    private val fetchPagedMessagesUseCase: FetchPagedMessagesUseCase,
    private val fetchUserUseCase: FetchUserUseCase,
    private val makeAVoiceCallUseCase: MakeAVoiceCallUseCase
) : BaseViewModel() {

    private val _userState = mutableUiStateFlow<User>()
    val userState = _userState.asStateFlow()

    fun sendMessage(
        id: String,
        receiverPhoneNumber: String,
        message: String,
        image: String,
        timeMessageWasSent: String,
        messageId: String,
    ) {
        viewModelScope.launch {
            sendMessageUseCase(
                id,
                receiverPhoneNumber,
                message,
                image,
                timeMessageWasSent,
                messageId
            )
        }
    }

    fun sendVoiceMessage(file: String, imageFileName: String) {
        viewModelScope.launch {
            sendVoiceMessageUseCase(file, imageFileName)
        }
    }

    fun fetchPagedMessages(senderPhoneNumber: String, receiverPhoneNumber: String) =
        fetchPagedMessagesUseCase(senderPhoneNumber, receiverPhoneNumber)

    fun fetchUser(phoneNumber: String) =
        fetchUserUseCase(phoneNumber).gatherRequest(_userState)

    fun makeAVoiceCall(
        callerId: String,
        calleeId: String,
        actionOnCallCreatedSuccessfully: (() -> Unit)? = null,
        actionOnCallConnected: (() -> Unit)? = null,
        actionOnCallEnded: (() -> Unit)? = null
    ) = makeAVoiceCallUseCase(
        callerId,
        calleeId,
        actionOnCallCreatedSuccessfully,
        actionOnCallConnected,
        actionOnCallEnded
    )
}