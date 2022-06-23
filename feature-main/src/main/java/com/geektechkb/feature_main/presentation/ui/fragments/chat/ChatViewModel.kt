package com.geektechkb.feature_main.presentation.ui.fragments.chat

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.geektechkb.core.base.BaseViewModel
import com.geektechkb.feature_main.domain.models.Message
import com.geektechkb.feature_main.domain.models.User
import com.geektechkb.feature_main.domain.useCases.FetchPagedMessagesUseCase
import com.geektechkb.feature_main.domain.useCases.FetchUserUseCase
import com.geektechkb.feature_main.domain.useCases.SendMessageUseCase
import com.geektechkb.feature_main.domain.useCases.UpdateUserStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val fetchPagedMessagesUseCase: FetchPagedMessagesUseCase,
    private val fetchUserUseCase: FetchUserUseCase,
    private val updateUserStatusUseCase: UpdateUserStatusUseCase
) : BaseViewModel() {
    private val _userState = mutableUiStateFlow<User>()
    val userState = _userState.asStateFlow()

    fun sendMessage(
        id: String,
        receiverPhoneNumber: String,
        message: String,
        timeMessageWasSent: String
    ) {
        viewModelScope.launch {
            sendMessageUseCase(id, receiverPhoneNumber, message, timeMessageWasSent)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun fetchPagedMessages(): Flow<PagingData<Message>> {
        val pagedMessages = fetchPagedMessagesUseCase() as Flow<PagingData<Message>>
        return pagedMessages.cachedIn(viewModelScope)
    }

    suspend fun fetchUser(phoneNumber: String) =
        fetchUserUseCase(phoneNumber).gatherRequest(_userState)

    fun updateUserStatus(status: String) = updateUserStatusUseCase(status)

    init {
        fetchPagedMessages()
    }
}