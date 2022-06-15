package com.geektechkb.feature_main.presentation.ui.fragments.chat

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.geektechkb.core.base.BaseViewModel
import com.geektechkb.feature_main.domain.models.Message
import com.geektechkb.feature_main.domain.typeAliases.NotAnActualPagingData
import com.geektechkb.feature_main.domain.useCases.FetchPagedMessagesUseCase
import com.geektechkb.feature_main.domain.useCases.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val fetchPagedMessagesUseCase: FetchPagedMessagesUseCase,
) : BaseViewModel() {
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

        fetchPagedMessagesUseCase().apply {
            this as Flow<PagingData<Message>>
            this.gatherPagingRequest { }
            return this
        }
    }
//        fetchPagedMessagesUseCase().checkPagingDataOf<Message>()
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T : Any> NotAnActualPagingData.checkPagingDataOf() =
    if (this is List<*> && this.all { it is T }) {
        this as Flow<PagingData<T>>
    } else null