package com.geektechkb.feature_main.presentation.ui.fragments.chat

import android.provider.ContactsContract.Contacts.Photo
import androidx.lifecycle.viewModelScope
import com.geektechkb.core.base.BaseViewModel
import com.geektechkb.feature_main.domain.models.User
import com.geektechkb.feature_main.domain.useCases.FetchPagedMessagesUseCase
import com.geektechkb.feature_main.domain.useCases.FetchUserUseCase
import com.geektechkb.feature_main.domain.useCases.SendMessageUseCase
import com.geektechkb.feature_main.domain.useCases.SendVoiceMessageUseCase
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
) : BaseViewModel() {

    private val _userState = mutableUiStateFlow<User>()
    val userState = _userState.asStateFlow()
    private val _photoUiState = mutableUiStateFlow<Photo>()
    val photoState = _photoUiState.asStateFlow()

    fun sendMessage(
        id: String,
        receiverPhoneNumber: String,
        message: String,
        image: String?,
        mediaType: String? = null,
        videoDuration: String? = null,
        timeMessageWasSent: String,
        messageId: String,
        onSuccess: (() -> Unit)? = null
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
                messageId,
                onSuccess
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

}