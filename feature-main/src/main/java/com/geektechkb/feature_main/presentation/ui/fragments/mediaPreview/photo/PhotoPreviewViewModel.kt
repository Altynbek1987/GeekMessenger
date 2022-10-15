package com.geektechkb.feature_main.presentation.ui.fragments.mediaPreview.photo

import com.geektechkb.core.base.BaseViewModel
import com.geektechkb.feature_main.domain.useCases.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PhotoPreviewViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase
) : BaseViewModel() {
//    fun sendMessage(
//        id: String,
//        receiverPhoneNumber: String,
//        message: String,
//        image: String,
//        mediaType: String?,
//        timeMessageWasSent: String,
//        messageId: String,
//    ) {
//        viewModelScope.launch {
//            sendMessageUseCase(
//                id,
//                receiverPhoneNumber,
//                message,
//                image,
//                mediaType,
//                timeMessageWasSent,
//                messageId
//            )
//        }
//    }
}