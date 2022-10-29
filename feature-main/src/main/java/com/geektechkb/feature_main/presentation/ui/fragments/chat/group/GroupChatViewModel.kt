package com.geektechkb.feature_main.presentation.ui.fragments.chat.group

import androidx.lifecycle.viewModelScope
import com.geektechkb.core.base.BaseViewModel
import com.geektechkb.feature_main.domain.models.Group
import com.geektechkb.feature_main.domain.useCases.FetchGroupMessagesUseCase
import com.geektechkb.feature_main.domain.useCases.GetGroupInfoUseCase
import com.geektechkb.feature_main.domain.useCases.SendMessageToGroupUseCase
import com.geektechkb.feature_main.domain.useCases.SendVoiceMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupChatViewModel @Inject constructor(
    private val fetchGroupMessagesUseCase: FetchGroupMessagesUseCase,
    private val sendVoiceMessageUseCase: SendVoiceMessageUseCase,
    private val sendMessageToGroupUseCase: SendMessageToGroupUseCase,
    private val getGroupInfoUseCase: GetGroupInfoUseCase
) : BaseViewModel() {

    private val _groupInfoState = mutableUiStateFlow<Group>()
    val groupInfoState = _groupInfoState.asStateFlow()

    fun fetchGroupMessages(groupName: String) = fetchGroupMessagesUseCase(groupName)

    fun getGroupInfo(groupName: String) =
        getGroupInfoUseCase(groupName).gatherRequest(_groupInfoState)


    fun sendMessageToGroup(
        groupName: String,
        senderPhoneNumber: String,
        receiversPhoneNumbers: List<String>,
        message: String,
        timeMessageWasSent: String,
        messageId: String,
    ) {
        viewModelScope.launch {
            sendMessageToGroupUseCase(
                groupName,
                senderPhoneNumber,
                receiversPhoneNumbers,
                message,
                timeMessageWasSent,
                messageId,
            )
        }
    }

    fun sendVoiceMessage(file: String, imageFileName: String) {
        viewModelScope.launch {
            sendVoiceMessageUseCase(file, imageFileName)
        }
    }
}