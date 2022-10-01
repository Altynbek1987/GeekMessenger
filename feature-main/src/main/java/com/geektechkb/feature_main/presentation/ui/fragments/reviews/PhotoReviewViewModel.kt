package com.geektechkb.feature_main.presentation.ui.fragments.reviews

import androidx.lifecycle.viewModelScope
import com.geektechkb.core.base.BaseViewModel
import com.geektechkb.feature_main.domain.useCases.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoReviewViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase
) : BaseViewModel() {
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
}